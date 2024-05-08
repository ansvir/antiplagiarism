package com.example.antiplagiarism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AntiplagiarismWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntiplagiarismWebApplication.class, args);
    }

}
