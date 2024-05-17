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

    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword(), USER_ROLE, user.isEnabled());
    }

    public User toEntity(UserDto user) {
        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setEnabled(user.isEnabled());
        return userEntity;
    }

}
