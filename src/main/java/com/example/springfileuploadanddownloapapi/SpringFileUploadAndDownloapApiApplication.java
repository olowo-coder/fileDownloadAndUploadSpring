package com.example.springfileuploadanddownloapapi;

import com.example.springfileuploadanddownloapapi.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SpringFileUploadAndDownloapApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFileUploadAndDownloapApiApplication.class, args);
    }

}
