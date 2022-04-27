package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.ApkDetails;

@Repository
public interface ApkDeviceRepository extends JpaRepository<ApkDetails, Long> {

}