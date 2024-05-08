package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextTestDto {

    private Long id;
    private String textOne;
    private String textTwo;
    private BigDecimal plagiatResult;

}
