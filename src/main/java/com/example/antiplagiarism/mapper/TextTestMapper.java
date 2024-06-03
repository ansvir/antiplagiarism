package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextTestMapper {

    private final TriadaMapper triadaMapper;

    public TextTestDto toDto(TextTest textTest) {
        return new TextTestDto(textTest.getId(), textTest.getTextOne(), textTest.getTextTwo(),
                textTest.getPlagiatResult(),
                textTest.getTriads()
                        .stream()
                        .map(triadaMapper::toDto)
                        .collect(Collectors.toList()));
    }

    public TextTest toEntity(TextTestDto textTestDto) {
        return new TextTest(textTestDto.getId(), textTestDto.getTextOne(),
                textTestDto.getTextTwo(), textTestDto.getPlagiatResult(),
                textTestDto.getTriads()
                        .stream()
                        .map(triadaMapper::toEntity)
                        .collect(Collectors.toList()));
    }

}
