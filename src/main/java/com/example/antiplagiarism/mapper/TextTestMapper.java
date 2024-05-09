package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextTestMapper {

    private final ProfileMapper profileMapper;

    public TextTestDto toDto(TextTest textTest) {
        return new TextTestDto(textTest.getId(), textTest.getTextOne(), textTest.getTextTwo(),
                textTest.getPlagiatResult(), textTest.getProfileId().getId());
    }

    public TextTest toEntity(TextTestDto textTestDto, ProfileDto profileDto) {
        return new TextTest(textTestDto.getId(), textTestDto.getTextOne(),
                textTestDto.getTextTwo(), textTestDto.getPlagiatResult(), profileMapper.toEntity(profileDto));
    }

}
