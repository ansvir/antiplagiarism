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
    @NotEmpty(message = "Username must not be empty!")
    @Length(min = 2, max = 255, message = "Username must be 2 to 255 characters long!")
    private String username;
    @NotNull
    @NotEmpty(message = "Password must not be empty!")
    @Length(min = 2, max = 255, message = "Password must be 2 to 255 characters long!")
    private String password;

}
