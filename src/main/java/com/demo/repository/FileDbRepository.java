package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.DbFile;

@Repository
public interface FileDbRepository extends JpaRepository<DbFile, String>{

	
	
}
