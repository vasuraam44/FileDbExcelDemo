package com.demo.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.demo.model.Device;
import com.demo.service.ExcelService;

@RestController
public class ExcelController {
	
	@Autowired
	ExcelService fileService;
	
	@CrossOrigin()
	@PostMapping("/")
	  public String Connection() {
		return "Connected Successfully";
	    
	  }
	@CrossOrigin()
	@GetMapping("/")
	  public String ConnectionRequest() {
		return "Connected Successfully";
	    
	  }

	
	
		@CrossOrigin()
	  @PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("files") MultipartFile[] files) {
	    String message = "";
	    
	    //MultipartFile file1=files[0];
	    if (ExcelHelper.hasExcelFormat(files[0]) || ExcelHelper.hasExcelFormat(files[1])) {
	      try {
	    	  
	    	 // System.out.println("------------------------------------------------------------");
	    	  //System.out.println(files[0].getContentType().equals("application/vnd.android.package-archive"));
	    	 // System.out.println(files[0].getContentType().equals("application/vnd.android.package-archive")||files[1].getContentType().equals("application/vnd.android.package-archive"));
	    	  if(files[0].getContentType().equals("application/vnd.android.package-archive") || files[1].getContentType().equals("application/vnd.android.package-archive")) {
	    		// MultipartFile file2=files[0];
	    		 
	  	        fileService.saveToDevice(files);
	  	        //System.out.println("------------------------------------------------------------");
	  	        message = "Uploaded the file successfully";
	  	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    	  }
	    	  else {
	    		  
	    		  message = "Please upload an APK File!";
	    		  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	    	  }
	    	  
	
	    	 
	      } catch (Exception e) {
	        message = "Could not upload the files: " + files[0].getOriginalFilename() + " ,"+files[1].getOriginalFilename()+"!";
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
	      URI uri=new URI("http://"+device.getUrl());
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
	      Device device = fileService.getDevicesByDeviceId(deviceid);
	      URI uri=new URI("http://"+device.getUrl());
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
	      Device device = fileService.getDevicesByDeviceId(deviceid);
	      if (device.equals(null)) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(new URI("http://"+device.getUrl()), HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	  
	  @CrossOrigin()
	  @GetMapping("/api/deviceurl")
	  public ResponseEntity<URI> getDeviceUrlByJsonDeviceId(@RequestBody Device deviceid) {
	    try {
	      Device device = fileService.getDevicesByDeviceId(deviceid.getDeviceNo());
	      if (device.equals(null)) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(new URI("http://"+device.getUrl()), HttpStatus.OK);
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
	    	  Device device=apkDetails2.getDevice();
	    	  URI uri=new URI("http://"+device.getUrl());
		      device.setUrl(uri.toString());
		      apkDetails2.setDevice(device);
	    	  
		}
	      
	      
	      return new ResponseEntity<>(apkDetails, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	

}
