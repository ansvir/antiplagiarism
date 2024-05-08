package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.service.model.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.antiplagiarism.repository.entity.Profile;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final UserMapper userMapper;
    private final TextTestMapper textTestMapper;

    public ProfileDto toDto(Profile profile) {
        return new ProfileDto(profile.getId(), userMapper.toDto(profile.getUser()), profile.getTextTests()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList()));
    }

    public Profile toEntity(ProfileDto profileDto) {
        return new Profile(profileDto.getId(),
                userMapper.toEntity(profileDto.getUserDto()), profileDto.getTextTestDtos()
                .stream()
                .map(textTestMapper::toEntity)
                .collect(Collectors.toList()));
    }

}
