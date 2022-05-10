package com.demo.responseEntity;

import java.sql.Timestamp;


public class ResponseModel {
	
	String  time_stamp;
	
	String request_type;
	
	DeviceInfo Device_info;
	
	FileInfo File_info;

	

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getRequest_type() {
		return request_type;
	}

	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}

	public DeviceInfo getDevice_info() {
		return Device_info;
	}

	public void setDevice_info(DeviceInfo device_info) {
		Device_info = device_info;
	}

	public FileInfo getFile_info() {
		return File_info;
	}

	public void setFile_info(FileInfo file_info) {
		File_info = file_info;
	}
	
	

}
