package com.example.springfileuploadanddownloapapi.service;

import com.example.springfileuploadanddownloapapi.property.FileStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Slf4j
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        fileStorageLocation = Path.of(fileStorageProperties.getUploadDir());
        try{
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            log.error("could not create directory", e);
        }
    }


    public String storeFile(MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            Path targetLocation = Path.of(fileStorageLocation.toString()).resolve(fileName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }


    public Resource getFile(String fileName){
        try{
            Path filePath = Path.of(fileStorageLocation.toString()).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()){
                return  resource;
            }
            else throw new RuntimeException("file not found");
        } catch (MalformedURLException e) {
            throw new RuntimeException("file not found", e);
        }
    }
}
