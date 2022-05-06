package com.demo.responseEntity;



public class RequestFileInfo {
	
	private String file_type;
	
	private String currentversion;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getCurrentversion() {
		return currentversion;
	}

	public void setCurrentversion(String currentversion) {
		this.currentversion = currentversion;
	}

	@Override
	public String toString() {
		return "RequestFileInfo [file_type=" + file_type + ", currentversion=" + currentversion + "]";
	}

	

	

	

}
