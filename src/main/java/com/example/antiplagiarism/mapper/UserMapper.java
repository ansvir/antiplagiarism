package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.User;
import com.example.antiplagiarism.service.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.example.antiplagiarism.util.AntiplagiarismUtil.USER_ROLE;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final TextTestMapper textTestMapper;

    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword(), USER_ROLE,
                user.getTextTests().stream().map(textTestMapper::toDto).collect(Collectors.toList()),
                user.isEnabled());
    }

    public User toEntity(UserDto user) {
        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setTextTests(user.getTextTests()
                .stream().map(textTestMapper::toEntity)
                .collect(Collectors.toList()));
        userEntity.setEnabled(user.isEnabled());
        return userEntity;
    }

}
