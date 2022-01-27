package com.example.springfileuploadanddownloapapi.controller;

import com.example.springfileuploadanddownloapapi.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile multipartFile){
        return ResponseEntity.ok(fileService.storeFile(multipartFile));

    }


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename, HttpServletRequest request){
        Resource fileResource = fileService.getFile(filename);
        try {
            String contentType;
            contentType = request.getServletContext().getMimeType(fileResource.getFile().getAbsolutePath());
            if(contentType == null){
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""+ fileResource.getFilename() + "\"")
                    .body(fileResource);
        } catch (IOException e) {
            throw  new RuntimeException("Could not determine file type", e);
        }

    }

}
