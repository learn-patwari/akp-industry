package com.example.s3hierarchy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class S3HierarchyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(S3HierarchyApiApplication.class, args);
    }
}
