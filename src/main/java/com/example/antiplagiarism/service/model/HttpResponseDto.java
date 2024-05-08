package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseDto {

    private String content;
    private int statusCode;

}
