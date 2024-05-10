package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextTestSubmitDto {

    @NotEmpty(message = "Entered text must not be empty!")
    private String textOne;

    @NotEmpty(message = "Entered text must not be empty!")
    private String textTwo;

}
