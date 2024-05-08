package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private Long id;
    private UserDto userDto;
    private List<TextTestDto> textTestDtos;

}
