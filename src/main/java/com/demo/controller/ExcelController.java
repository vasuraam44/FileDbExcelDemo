package com.demo.controller;

import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.excel.ExcelHelper;
import com.demo.excel.ResponseMessage;
import com.demo.model.ApkDetails;
import com.demo.model.DbFile;
import com.demo.model.Device;
import com.demo.responseEntity.DeviceInfo;
import com.demo.responseEntity.FileInfo;
import com.demo.responseEntity.RequestEntity;
import com.demo.responseEntity.RequestFileInfo;
import com.demo.responseEntity.RequestSuccessModel;
import com.demo.responseEntity.ResponseAck;
import com.demo.responseEntity.ResponseModel;
import com.demo.service.ExcelService;
import com.demo.service.FileDbService;

@RestController
public class ExcelController {

	@Autowired
	ExcelService fileService;

	@Autowired
	FileDbService fileDbService;

	@CrossOrigin()
	@PostMapping("/")
	public String Connection() {

		return "Connected Successfully";

	}

	@CrossOrigin()
	@GetMapping("/")
	public String ConnectionRequest() {
		return "Connected Sucessfully";

	}

	@CrossOrigin()
	@GetMapping("/check")
	public RequestEntity RequestResponseCheck() {

//		ResponseModel responseModel=new ResponseModel();
//		responseModel.setDevice_info(new DeviceInfo());
//		responseModel.setFile_info(new FileInfo() );
//		return responseModel;

		RequestEntity requestEntity = new RequestEntity();
		requestEntity.setDevice_info(new DeviceInfo());
		requestEntity.setFile_info(new RequestFileInfo());
		return requestEntity;

	}

	// ----- File Upload

	@CrossOrigin()
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("files") MultipartFile[] files,
			@RequestParam("version") String version) {
		String message = "";

	
		if (ExcelHelper.hasExcelFormat(files[0]) || ExcelHelper.hasExcelFormat(files[1])) {
			try {

				
				if (files[0].getContentType().equals("application/vnd.android.package-archive")
						|| files[1].getContentType().equals("application/vnd.android.package-archive")) {
					

					fileService.saveToDevice(files, version);
					
					message = "Uploaded the file successfully";
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
				} else {

					message = "Please upload an APK File!";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
				}

			} catch (Exception e) {
				message = "Could not upload the files: " + files[0].getOriginalFilename() + " ,"
						+ files[1].getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@CrossOrigin()
	@PostMapping("/files/upload")
	public ResponseEntity<ResponseMessage> uploadAllFile(@RequestParam("excelfile") MultipartFile excelfile,
			@RequestParam("apkfile") MultipartFile apkfile, @RequestParam("evtfile") MultipartFile evtfile,
			@RequestParam("evtpfile") MultipartFile evtpfile, @RequestParam("evtpsfile") MultipartFile evtpsfile,
			@RequestParam("version") String version) {

		String message = "";
		
		// MultipartFile file1=files[0];
		if (ExcelHelper.hasExcelFormat(excelfile)) {
			try {

				// fileService.saveToDevice(files, version);
				fileService.saveFileDB(excelfile, apkfile, evtfile, evtpfile, evtpsfile, version);

				
				message = "Uploaded the file successfully";
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

			} catch (Exception e) {
				message = "Could not upload the files: !";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@CrossOrigin()
	@PostMapping("/file/uploadevt")
	public ResponseEntity<ResponseMessage> uploadEvtFile(@RequestParam("excelfile") MultipartFile excelfile,
			@RequestParam("evtfile") MultipartFile evtfile, @RequestParam("version") String version,
			@RequestParam("notemessage") String notemessage) {

		String message = "";
		
		if (ExcelHelper.hasExcelFormat(excelfile)) {
			try {

				// fileService.saveToDevice(files, version);
				fileService.saveEvtFileDB(excelfile, evtfile, version, notemessage);

				
				message = "Uploaded the file successfully";
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

			} catch (Exception e) {
				message = "Could not upload the files: !";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@GetMapping("/devices")
	public ResponseEntity<List<Device>> getAllDevices() {
		try {
			List<Device> devices = fileService.getAllDevices();
			if (devices.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(devices, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/device/{id}")
	public ResponseEntity<Device> getDeviceById(@PathVariable long id) {
		try {
			Device device = fileService.getDevicesById(id);
			URI uri = new URI("http://" + device.getUrl());
			device.setUrl(uri.toString());
			if (device.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(device, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/devices/device/{deviceid}")
	public ResponseEntity<Device> getDeviceByDeviceId(@PathVariable String deviceid) {
		try {
			Device device = fileService.getDevicesByDeviceId1(deviceid);
			URI uri = new URI("http://" + device.getUrl());
			device.setUrl(uri.toString());
			if (device.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(device, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin()
	@GetMapping("/deviceurl/{deviceid}")
	public ResponseEntity<URI> getDeviceUrlByDeviceId(@PathVariable String deviceid) {
		try {
			Device device = fileService.getDevicesByDeviceId1(deviceid);
			if (device.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(new URI("http://" + device.getUrl()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin()
	@GetMapping("/api/deviceurl")
	public ResponseEntity<URI> getDeviceUrlByJsonDeviceId(@RequestBody Device deviceid) {
		try {
			Device device = fileService.getDevicesByDeviceId1(deviceid.getDeviceNo());
			if (device == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(new URI("http://" + device.getUrl()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// android JSON request ,JSON response

	@CrossOrigin()
	@PostMapping("/api/deviceurl")
	public ResponseEntity<String> getDeviceUrlByJsonDevicePostId(@RequestBody Device deviceid) {
		try {
			Device device = fileService.getDevicesByDeviceId1(deviceid.getDeviceNo());
			if (device.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>("http://" + device.getUrl(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ANDROID JSON REQUEST , JSON RESPONSE Given format

	@CrossOrigin()
	@PostMapping("/api/jsonresponse")
	public ResponseEntity<ResponseModel> getResponseJson(@RequestBody RequestEntity request) {
		try {

			ResponseModel responseModel = new ResponseModel();
			System.out.println(request);

			System.out.println("SERIAL NUMBER:" + request.getDevice_info().getSerialnumber());
			Device device = fileService.getDevicesByDeviceId(request.getDevice_info().getSerialnumber());
			System.out.println(device);
			if (device == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			} else {
				if (request.getFile_info().getFile_type().equals("evt")) {

					// ResponseModel responseModel=new ResponseModel();
					LocalDateTime dateTime = LocalDateTime.now();
					String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

					// Timestamp timestamp = new Timestamp(timeStamp);
					responseModel.setTime_stamp(timeStamp);
					responseModel.setRequest_type(request.getRequest_type());
					responseModel.setDevice_info(request.getDevice_info());

					// file type
					String filetype = request.getFile_info().getFile_type();

					if (device.getEvtfileId() == null) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					// updating APK details Current version and status
					System.out.println("==================APKDETAILS============================");
					// ApkDetails apkDetails=apkDeviceRepository.findByDevice(device);
					String currentversion = request.getFile_info().getCurrentversion();

					DbFile dbFile = fileDbService.getFile(device.getEvtfileId());

					// new version
					String newVersion = dbFile.getVersion();
					if (currentversion.compareTo(newVersion) == 1) {

						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					fileService.UpdateApkDetails(device, currentversion);

					// file Size in MB
					long sizebytes = dbFile.getSize();
					long sizeMB = sizebytes / (1024 * 1024);
					String fileSize = String.valueOf(sizeMB);

					// file Status
					// String FileStatus=dbFile.getFile_status();
					String fileStatus = "Available";

					// file link
					InetAddress ip = InetAddress.getLocalHost();
					String IpAddress = ip.getHostAddress();
					String filelink = "http://" + IpAddress + ":8080/downloadFile/" + dbFile.getId();
					System.out.println("------------------------------------------------------------");
					System.out.println("FILE LINK: " + filelink);

					// release message
					// String message=dbFile.getMessage();
					String message = "Releasing Note here this was the new version APK file to update your device...";

					FileInfo fileInfo = new FileInfo();
					fileInfo.setFile_type(filetype);
					fileInfo.setNewVersion(newVersion);
					fileInfo.setFile_size(fileSize + " MB");
					fileInfo.setFile_status(fileStatus);
					fileInfo.setFile_link(filelink);
					fileInfo.setMessage(message);

					responseModel.setFile_info(fileInfo);

					return new ResponseEntity<>(responseModel, HttpStatus.OK);

				} else if (request.getFile_info().getFile_type().equals("evtp")) {

					// ResponseModel responseModel=new ResponseModel();
					LocalDateTime dateTime = LocalDateTime.now();
					String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

					// Timestamp timestamp = new Timestamp(timeStamp);
					responseModel.setTime_stamp(timeStamp);
					responseModel.setRequest_type(request.getRequest_type());
					responseModel.setDevice_info(request.getDevice_info());
					// file type
					String filetype = request.getFile_info().getFile_type();

					if (device.getEvtpfileId() == null) {

						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					// updating APK details Current version and status
					System.out.println("==================APKDETAILS============================");
					// ApkDetails apkDetails=apkDeviceRepository.findByDevice(device);
					String currentversion = request.getFile_info().getCurrentversion();
					fileService.UpdateApkDetails(device, currentversion);
					DbFile dbFile = fileDbService.getFile(device.getEvtpfileId());

					// new version
					String newVersion = dbFile.getVersion();

					if (currentversion.compareTo(newVersion) == 1) {

						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					// file Size in MB
					long sizebytes = dbFile.getSize();
					long sizeMB = sizebytes / (1024 * 1024);
					String fileSize = String.valueOf(sizeMB);

					// file Status
					// String FileStatus=dbFile.getFile_status();
					String fileStatus = "Available";

					// file link
					InetAddress ip = InetAddress.getLocalHost();
					String IpAddress = ip.getHostAddress();
					String filelink = "http://" + IpAddress + ":8080/downloadFile/" + dbFile.getId();
					System.out.println("------------------------------------------------------------");
					System.out.println("FILE LINK: " + filelink);

					// release message
					// String message=dbFile.getMessage();
					String message = "Releasing Note here this was the new version APK file to update your device...";

					FileInfo fileInfo = new FileInfo();
					fileInfo.setFile_type(filetype);
					fileInfo.setNewVersion(newVersion);
					fileInfo.setFile_size(fileSize + " MB");
					fileInfo.setFile_status(fileStatus);
					fileInfo.setFile_link(filelink);
					fileInfo.setMessage(message);

					responseModel.setFile_info(fileInfo);

					return new ResponseEntity<>(responseModel, HttpStatus.OK);

				} else {

					// ResponseModel responseModel=new ResponseModel();
					LocalDateTime dateTime = LocalDateTime.now();
					String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

					// Timestamp timestamp = new Timestamp(timeStamp);
					responseModel.setTime_stamp(timeStamp);
					responseModel.setRequest_type(request.getRequest_type());
					responseModel.setDevice_info(request.getDevice_info());
					// file type
					String filetype = request.getFile_info().getFile_type();

					if (device.getApkfileId() == null) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					// updating APK details Current version and status
					System.out.println("==================APKDETAILS============================");
					// ApkDetails apkDetails=apkDeviceRepository.findByDevice(device);
					String currentversion = request.getFile_info().getCurrentversion();
					fileService.UpdateApkDetails(device, currentversion);

					DbFile dbFile = fileDbService.getFile(device.getApkfileId());

					// new version
					String newVersion = dbFile.getVersion();

					if (currentversion.compareTo(newVersion) == 1) {

						FileInfo fileInfo = new FileInfo();
						fileInfo.setFile_type(filetype);
						fileInfo.setFile_status("Not availabale");
						responseModel.setFile_info(fileInfo);

						return new ResponseEntity<>(responseModel, HttpStatus.OK);

					}

					// file Size in MB
					long sizebytes = dbFile.getSize();
					long sizeMB = sizebytes / (1024 * 1024);
					String fileSize = String.valueOf(sizeMB);

					// file Status
					// String FileStatus=dbFile.getFile_status();
					String fileStatus = "Available";

					// file link
					InetAddress ip = InetAddress.getLocalHost();
					String IpAddress = ip.getHostAddress();
					String filelink = "http://" + IpAddress + ":8080/downloadFile/" + dbFile.getId();
					System.out.println("------------------------------------------------------------");
					System.out.println("FILE LINK: " + filelink);

					// release message
					// String message=dbFile.getMessage();
					String message = "Releasing Note here this was the new version APK file to update your device...";

					FileInfo fileInfo = new FileInfo();
					fileInfo.setFile_type(filetype);
					fileInfo.setNewVersion(newVersion);
					fileInfo.setFile_size(fileSize + " MB");
					fileInfo.setFile_status(fileStatus);
					fileInfo.setFile_link(filelink);
					fileInfo.setMessage(message);

					responseModel.setFile_info(fileInfo);
					return new ResponseEntity<>(responseModel, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unused")
	@CrossOrigin()
	@PostMapping("/api/downlod/jsonresponse")
	public ResponseEntity<ResponseAck> getSucessResponseJson(@RequestBody RequestSuccessModel request) {
		try {
			System.out.println(request);
			
			
			if (request.getStatus_info().getStatus().equalsIgnoreCase("success")) {

				ApkDetails apkDetails = fileService.UpdateApkDetailsStatus(request);
				System.out.println("================================");
				System.out.println(apkDetails.getStatus());
				if (apkDetails == null) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);

				}

				LocalDateTime dateTime = LocalDateTime.now();
				String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

				ResponseAck responseAck = new ResponseAck();
				responseAck.setTime_stamp(timeStamp);
				responseAck.setRequest_type(request.getRequest_type());
				responseAck.setDevice_info(request.getDevice_info());
				responseAck.setAck_info("Success");
				return new ResponseEntity<>(responseAck, HttpStatus.OK);

			} else {
				ApkDetails apkDetails = fileService.UpdateApkDetailsStatus(request);
				System.out.println("================================");
				System.out.println(apkDetails.getStatus());
				LocalDateTime dateTime = LocalDateTime.now();
				String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

				ResponseAck responseAck = new ResponseAck();
				responseAck.setTime_stamp(timeStamp);
				responseAck.setRequest_type(request.getRequest_type());
				responseAck.setDevice_info(request.getDevice_info());
				responseAck.setAck_info("Failed");
				return new ResponseEntity<>(responseAck, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin()
	@GetMapping("/apkdetails")
	public ResponseEntity<List<ApkDetails>> getAllapkDetails() {

		try {
			List<ApkDetails> apkDetails = fileService.getAllApkDetails();
			if (apkDetails.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			for (ApkDetails apkDetails2 : apkDetails) {
				Device device = apkDetails2.getDevice();
				URI uri = new URI("http://" + device.getUrl());
				device.setUrl(uri.toString());
				apkDetails2.setDevice(device);

			}

			return new ResponseEntity<>(apkDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	

}


//Excel Controller Note for NEW BRANCH CHECK 

