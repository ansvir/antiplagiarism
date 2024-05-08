package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.UserMapper;
import com.example.antiplagiarism.repository.db.UserRepository;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.service.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IService<UserDto, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto user) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public void updateAll(List<UserDto> entities) {
        userRepository.saveAll(entities.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList()));
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    public boolean checkUserExists(UserAuthDto userAuthDto) {
        try {
            UserDto foundUser = findByUsername(userAuthDto.getUsername());
            return passwordEncoder.matches(foundUser.getPassword(), userAuthDto.getPassword());
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private UserDto findByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No such user: " + username)));
    }


}
