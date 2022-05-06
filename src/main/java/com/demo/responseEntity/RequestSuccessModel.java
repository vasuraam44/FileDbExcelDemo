

package com.demo.responseEntity;


public class RequestSuccessModel {
	

	String time_stamp;
	
	String request_type;
	
	DeviceInfo device_info;
	
	StatusInfo status_info;

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

	public StatusInfo getStatus_info() {
		return status_info;
	}

	public void setStatus_info(StatusInfo status_info) {
		this.status_info = status_info;
	}

	@Override
	public String toString() {
		return "RequestSuccessModel [time_stamp=" + time_stamp + ", request_type=" + request_type + ", device_info="
				+ device_info + ", status_info=" + status_info + "]";
	}

	
	
	
	
	

}
