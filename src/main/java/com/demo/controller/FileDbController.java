package com.demo.controller;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.excel.ExcelRW;
import com.demo.model.DbFile;
import com.demo.model.UploadFileResponse;
import com.demo.responseEntity.FileInfo;
import com.demo.service.FileDbService;


@RestController
public class FileDbController {
	
	@Autowired
	FileDbService fileDbService;
	
	
		//http://localhost:8080/api/save_file
		@PostMapping("/api/save_file/{deviceId}")
		public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,@PathVariable String deviceId) throws IOException {
			
			DbFile dbFile = fileDbService.storeFile(file,deviceId);

	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/downloadFile/")
	                .path(dbFile.getId())
	                .toUriString();
	        if(dbFile!=null) {
            	ExcelRW excelRW=new ExcelRW();
    			excelRW.writeLinkToExcel(deviceId, fileDownloadUri);
            }
			
	        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
	                file.getContentType(), file.getSize());
	      
		}
		
		@GetMapping("/downloadFile/{fileId}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
	        // Load file from database
	        DbFile dbFile = fileDbService.getFile(fileId);

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
	                .body(new ByteArrayResource(dbFile.getData()));
	        
	    }
		
		
		@GetMapping("/downloadFile/allFiles")
	    public List<UploadFileResponse> getAll() {
	        // Load file from database
	        List<DbFile> dbFiles = fileDbService.getAllfiles();
	        
	        List<UploadFileResponse> fileUris= dbFiles.stream().map(file -> {
	        	
		        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		                .path("/downloadFile/")
		                .path(file.getId())
		                .toUriString();
		        String fileName=file.getFileName();
		        String fileContenttype=file.getFileType();
		        long filesize=file.getSize();
		        UploadFileResponse fileUri=new UploadFileResponse(fileName, fileDownloadUri, fileContenttype, filesize);
		        //fileUris.add(fileUri);
		        return fileUri;
	        }).collect(Collectors.toList());
	        
	        return fileUris;
	        
			
	    }
		

		@CrossOrigin("http://localhost:3000")
		@GetMapping("/getAvailable/allFiles")
	    public List<FileInfo> getAvailableAll() throws UnknownHostException {
	        // Load file from database
	        List<DbFile> dbFiles = fileDbService.getAllfiles();
	        // file link
	    	InetAddress ip = InetAddress.getLocalHost();
	    	String IpAddress = ip.getHostAddress();
	    	String IP_PORT_URL="http://"+IpAddress+":8080/downloadFile/";
	        
	        List<FileInfo> fileinfos= dbFiles.stream().map(file -> {
	        	
	        	FileInfo fileInfo=new FileInfo();
	        	fileInfo.setFile_type(file.getFileName());
	        	fileInfo.setNewVersion(file.getVersion());
	        	long filesize=file.getSize()/(1024);
	        	fileInfo.setFile_size(String.valueOf(filesize+"KB"));
	        	fileInfo.setFile_status(file.getFile_status());
	        	fileInfo.setFile_link(IP_PORT_URL+file.getId());
	        	fileInfo.setMessage(file.getMessage());
	        
	        	return fileInfo;
	        }).collect(Collectors.toList());
	        
	        return fileinfos;
	    }
	
		
		@GetMapping("/file")
	    public String WriteCheck() throws EncryptedDocumentException, IOException {
	        
			ExcelRW excelRW=new ExcelRW();
			
			excelRW.writeLinkToExcel("D1", "Link1");
			return "readExcelSucess";
	    }
		
	
	
	
	

}
