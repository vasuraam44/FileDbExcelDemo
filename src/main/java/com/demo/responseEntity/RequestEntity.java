package com.demo.responseEntity;

public class RequestEntity {
	
	
	String time_stamp;
	
	String request_type;
	
	DeviceInfo device_info;
	
	RequestFileInfo file_info;

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
		return device_info;
	}

	public void setDevice_info(DeviceInfo device_info) {
		this.device_info = device_info;
	}

	public RequestFileInfo getFile_info() {
		return file_info;
	}

	public void setFile_info(RequestFileInfo file_info) {
		this.file_info = file_info;
	}

	@Override
	public String toString() {
		return "RequestEntity [time_stamp=" + time_stamp + ", request_type=" + request_type + ", device_info="
				+ device_info + ", file_info=" + file_info + "]";
	}

	
	
	

}
