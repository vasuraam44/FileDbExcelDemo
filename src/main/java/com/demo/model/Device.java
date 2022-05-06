package com.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

	@Id
	long id;
	
	String deviceNo;
	
	String url;
	
	String apkfileId;
	
	String evtfileId;
	
	String evtpfileId;
	
	String evtpsfileId;
	
	
	
	
}
