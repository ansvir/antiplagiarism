package com.example.antiplagiarism.config;

import com.example.antiplagiarism.util.AntiplagiarismDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public AntiplagiarismDialect antiplagiarismDialect() {
        return new AntiplagiarismDialect();
    }

}
