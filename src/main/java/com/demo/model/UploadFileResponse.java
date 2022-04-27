package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {

	String fileName;
	String fileDownloadUri;
	String contentType;
	long size;
	
	
	
}
