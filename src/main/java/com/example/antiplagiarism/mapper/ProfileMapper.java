package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.antiplagiarism.repository.entity.Profile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final UserMapper userMapper;

    public ProfileDto toDto(Profile profile, List<TextTestDto> textTestDtos) {
        return new ProfileDto(profile.getId(), userMapper.toDto(profile.getUser()), textTestDtos);
    }

    public Profile toEntity(ProfileDto profileDto, List<TextTest> textTests) {
        return new Profile(profileDto.getId(),
                userMapper.toEntity(profileDto.getUserDto()), textTests);
    }

}
