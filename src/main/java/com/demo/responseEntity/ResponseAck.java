package com.demo.responseEntity;

public class ResponseAck {
	
	
	String time_stamp;
	
	String request_type;
	
	DeviceInfo device_info;
	
	String ack_info;

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

	public String getAck_info() {
		return ack_info;
	}

	public void setAck_info(String ack_info) {
		this.ack_info = ack_info;
	}
	
	

}
