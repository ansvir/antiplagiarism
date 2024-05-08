package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextTestSubmitDto {

    @NotNull
    @NotEmpty(message = "Entered text must not be empty!")
    private String textOne;
    @NotNull
    @NotEmpty(message = "Entered text must not be empty!")
    private String textTwo;

}
