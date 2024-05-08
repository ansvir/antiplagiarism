package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.User;
import com.example.antiplagiarism.service.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("USER")));
    }

    public User toEntity(UserDto user) {
        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEnabled(user.isEnabled());
        return userEntity;
    }

}
