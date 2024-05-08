package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TextTestMapper {

    public TextTestDto toDto(TextTest textTest) {
        return new TextTestDto(textTest.getId(), textTest.getTextOne(), textTest.getTextTwo(),
                textTest.getPlagiatResult());
    }

    public TextTest toEntity(TextTestDto textTestDto) {
        return new TextTest(textTestDto.getId(), textTestDto.getTextOne(),
                textTestDto.getTextTwo(), textTestDto.getPlagiatResult());
    }

}
