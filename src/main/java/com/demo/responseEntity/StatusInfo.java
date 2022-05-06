package com.demo.responseEntity;

public class StatusInfo {
	
	
	private String file_type;
	
	private String status;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StatusInfo [file_type=" + file_type + ", status=" + status + "]";
	}
	
	

}
