package com.demo.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.excel.ExcelHelper;
import com.demo.model.ApkDetails;
import com.demo.model.DbFile;
import com.demo.model.Device;
import com.demo.repository.ApkDeviceRepository;
import com.demo.repository.DeviceRepository;

@Service
public class ExcelService {

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	FileDbService fileDbService;

	@Autowired
	ApkDeviceRepository apkDeviceRepository;

	public void saveToDevice(MultipartFile[] files) {
		try {

			if (ExcelHelper.hasExcelFormat(files[0])) {
				List<Device> devices = ExcelHelper.excelToTutorials(files[0].getInputStream());
				DbFile dbFile = fileDbService.saveFile(files[1]);

				InetAddress ip = InetAddress.getLocalHost();
				String IpAddress = ip.getHostAddress();
				//String Baseurl="http://"+IpAddress+":8080";
				for (Device device : devices) {

					
					// http://localhost:3000/deviceview
					String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
							.path("/downloadFile/").path(dbFile.getId()).toUriString();
					device.setUrl(fileDownloadUri);

					System.out.println(fileDownloadUri);
					deviceRepository.save(device);
					this.saveApkDetails(device);
				}

			}else {
				List<Device> devices = ExcelHelper.excelToTutorials(files[1].getInputStream());
				DbFile dbFile = fileDbService.saveFile(files[0]);

				InetAddress ip = InetAddress.getLocalHost();
				String IpAddress = ip.getHostAddress();
				for (Device device : devices) {

					String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
							.path("/downloadFile/").path(dbFile.getId()).toUriString();
					device.setUrl(fileDownloadUri);

					deviceRepository.save(device);
					this.saveApkDetails(device);
				}
				
			}

		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public List<Device> getAllDevices() {
		return deviceRepository.findAll();
	}

	public Device getDevicesById(long id) {

		return deviceRepository.findById(id).get();
	}

	public Device getDevicesByDeviceId(String deviceid) {

		return deviceRepository.findByDeviceNo(deviceid).get();
	}

	public ApkDetails saveApkDetails(Device device) {

		ApkDetails apkDetails = new ApkDetails();
		apkDetails.setDevice(device);
		apkDetails.setStatus("pending");
		return apkDeviceRepository.save(apkDetails);
	}

	public List<ApkDetails> getAllApkDetails() {

		return apkDeviceRepository.findAll();
	}

}
