package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDto {

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 255)
    private String username;
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 255)
    private String password;

}
