package com.demo.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
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
import com.demo.responseEntity.RequestSuccessModel;

@Service
public class ExcelService {

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	FileDbService fileDbService;

	@Autowired
	ApkDeviceRepository apkDeviceRepository;

	public void saveToDevice(MultipartFile[] files, String version) {
		try {

			if (ExcelHelper.hasExcelFormat(files[0])) {
				List<Device> devices = ExcelHelper.excelToTutorials(files[0].getInputStream());
				DbFile dbFile = fileDbService.saveFile(files[1], version);

				InetAddress ip = InetAddress.getLocalHost();
				String IpAddress = ip.getHostAddress();
				// String Baseurl="http://"+IpAddress+":8080";

				DbFile evtdbFile = fileDbService.saveFile(files[2], version);

				for (Device device : devices) {

					// http://localhost:3000/deviceview
					String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
							.path("/downloadFile/").path(dbFile.getId()).toUriString();
					device.setUrl(fileDownloadUri);

					if (evtdbFile.getFileName().endsWith(".evt")) {
						device.setEvtfileId(evtdbFile.getId());
					} else if (evtdbFile.getFileName().endsWith(".evtp")) {
						device.setEvtpfileId(evtdbFile.getId());
					} else {
						device.setEvtpsfileId(evtdbFile.getId());
					}

					device.setApkfileId(dbFile.getId());

					System.out.println(fileDownloadUri);
					deviceRepository.save(device);
					System.out.println("----------------------------------------");
					ApkDetails dvs = apkDeviceRepository.findByDevice(device);

					System.out.println(dvs);
					if (dvs == null) {
						this.saveApkDetails(device);
					}

				}

			} else {
				List<Device> devices = ExcelHelper.excelToTutorials(files[1].getInputStream());
				DbFile dbFile = fileDbService.saveFile(files[0], version);

				InetAddress ip = InetAddress.getLocalHost();
				String IpAddress = ip.getHostAddress();
				for (Device device : devices) {

					String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
							.path("/downloadFile/").path(dbFile.getId()).toUriString();
					device.setUrl(fileDownloadUri);

					deviceRepository.save(device);
					System.out.println("----------------------------------------");
					ApkDetails dvs = apkDeviceRepository.findByDevice(device);

					System.out.println(dvs);
					if (dvs == null) {
						this.saveApkDetails(device);
					}

				}

			}

		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public void saveFileDB(MultipartFile excelfile, MultipartFile apkfile, MultipartFile evtfile,
			MultipartFile evtpfile, MultipartFile evtpsfile, String version) throws IOException {

		InetAddress ip = InetAddress.getLocalHost();
		String IpAddress = ip.getHostAddress();
		// String Baseurl="http://"+IpAddress+":8080";
		List<Device> devices = ExcelHelper.excelToTutorials(excelfile.getInputStream());
		DbFile apkdbFile = fileDbService.saveFile(apkfile, version);
		DbFile evtdbFile = fileDbService.saveFile(evtfile, version);
		DbFile evtpdbFile = fileDbService.saveFile(evtpfile, version);
		DbFile evtpsdbFile = fileDbService.saveFile(evtpsfile, version);
		
		for (Device device : devices) {

			// http://localhost:3000/deviceview
			String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
					.path("/downloadFile/").path(apkdbFile.getId()).toUriString();
			device.setUrl(fileDownloadUri);

			device.setEvtfileId(evtdbFile.getId());
			device.setEvtpfileId(evtpdbFile.getId());
			device.setEvtpsfileId(evtpsdbFile.getId());
			device.setApkfileId(apkdbFile.getId());
			
			System.out.println(fileDownloadUri);
			deviceRepository.save(device);
			System.out.println("----------------------------------------");
			ApkDetails dvs = apkDeviceRepository.findByDevice(device);

			System.out.println(dvs);
			if (dvs == null) {
				this.saveApkDetails(device);
			}

		}
		
		
	}

	
	
	
	public void saveEvtFileDB(MultipartFile excelfile, MultipartFile evtfile,
			 String version,String notemessage) throws IOException {

		InetAddress ip = InetAddress.getLocalHost();
		String IpAddress = ip.getHostAddress();
		// String Baseurl="http://"+IpAddress+":8080";
		List<Device> devices = ExcelHelper.excelToTutorials(excelfile.getInputStream());
		DbFile evtdbFile = fileDbService.saveEvtFile(evtfile, version,notemessage);
		
		for (Device device : devices) {

			String fileDownloadUri = ServletUriComponentsBuilder.fromPath(IpAddress + ":8080")
					.path("/downloadFile/").path(evtdbFile.getId()).toUriString();
			device.setUrl(fileDownloadUri);

			device.setEvtfileId(evtdbFile.getId());
			
			System.out.println(fileDownloadUri);
			deviceRepository.save(device);
			System.out.println("----------------------------------------");
			ApkDetails dvs = apkDeviceRepository.findByDevice(device);

			System.out.println(dvs);
			if (dvs == null) {
				this.saveApkDetails(device);
			}

		}
		
		
	}

	
	
	
	
	
	
	
	
	
	
	public List<Device> getAllDevices() {
		return deviceRepository.findAll();
	}

	public Device getDevicesById(long id) {

		return deviceRepository.findById(id).get();
	}

	public Device getDevicesByDeviceId(String deviceid) {

		return deviceRepository.findByDeviceNo(deviceid).orElse(null);
	}

	public Device getDevicesByDeviceId1(String deviceid) {

		return deviceRepository.findByDeviceNo(deviceid).orElse(null);
	}

	public ApkDetails saveApkDetails(Device device) {

		ApkDetails apkDetails = new ApkDetails();
		apkDetails.setDevice(device);
		apkDetails.setStatus("pending");
		return apkDeviceRepository.save(apkDetails);
	}
	
	public ApkDetails UpdateApkDetails(Device device,String curentversion) {
		
		System.out.println(device);
		ApkDetails apkDetails = apkDeviceRepository.findByDevice(device);
		System.out.println("==================APKDETAILS============================");
		System.out.println(apkDetails);
		//apkDetails.setDevice(device);
		apkDetails.setStatus("in progress");
		apkDetails.setCurrent_version(curentversion);
		
//		System.out.println("==============================================================");
//		System.out.println(apkDetails);
//		System.out.println("==============================================================");
		
		return apkDeviceRepository.save(apkDetails);
	}
	
	
	
	
	
	

	public List<ApkDetails> getAllApkDetails() {

		return apkDeviceRepository.findAll();
	}

	
	
	
	public ApkDetails UpdateApkDetailsStatus(RequestSuccessModel request) {
		
		Device device=deviceRepository.findByDeviceNo(request.getDevice_info().getSerialnumber()).get();
		System.out.println("======================");
		System.out.println(device);
		DbFile dbfile=fileDbService.getFile(device.getEvtfileId());
		
		ApkDetails apkDetails=apkDeviceRepository.findByDevice(device);
		if(request.getStatus_info().getStatus().equalsIgnoreCase("success")) {
		
		System.out.println("======================");
		System.out.println(apkDetails);
		apkDetails.setStatus(request.getStatus_info().getStatus());
		apkDetails.setDownloaded_version(dbfile.getVersion());
		return apkDeviceRepository.save(apkDetails);
		
		}else {
			System.out.println("======================");
			System.out.println(apkDetails);
			apkDetails.setStatus(request.getStatus_info().getStatus());
			apkDetails.setDownloaded_version(null);
			return apkDeviceRepository.save(apkDetails);
			
		}
	}
	
	

}
