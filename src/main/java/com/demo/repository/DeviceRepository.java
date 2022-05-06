package com.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>{

	Optional<Device> findByDeviceNo(String deviceid);

	//Device finBydeviceNo(String serialnumber);

	
	
}
