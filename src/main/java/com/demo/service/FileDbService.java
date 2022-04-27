package com.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.demo.model.DbFile;
import com.demo.repository.FileDbRepository;

import myexception.ResourceNotFoundException;

@Service
public class FileDbService {

	
	@Autowired
	FileDbRepository fileDbRepository;
	
	
	public DbFile storeFile(MultipartFile file,String deviceId) throws IOException {
		
		// Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
            DbFile dbFile = new DbFile();
            dbFile.setFileName(fileName);
            dbFile.setFileType(file.getContentType());
            dbFile.setData(file.getBytes());
            dbFile.setSize(file.getSize());

            DbFile savedDbFile=fileDbRepository.save(dbFile);
            
            return savedDbFile;
			
		
	}
	
	public DbFile getFile(String fileId) {
        return fileDbRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id " + fileId));
    }
	
	public List<DbFile> getAllfiles() {
        return fileDbRepository.findAll();
    }

	public DbFile saveFile(MultipartFile file) throws IOException {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
        DbFile dbFile = new DbFile();
        dbFile.setFileName(fileName);
        dbFile.setFileType(file.getContentType());
        dbFile.setData(file.getBytes());
        dbFile.setSize(file.getSize());

        DbFile savedDbFile=fileDbRepository.save(dbFile);
        
        return savedDbFile;
		
		
	}
	
}
