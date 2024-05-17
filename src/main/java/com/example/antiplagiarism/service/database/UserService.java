package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.UserMapper;
import com.example.antiplagiarism.repository.db.UserRepository;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.service.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IService<UserDto, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    public UserDto checkUserExists(UserAuthDto userAuthDto) {
        try {
            UserDto foundUser = findByUsername(userAuthDto.getUsername());
            if (!foundUser.getPassword().equals(userAuthDto.getPassword())) {
                return null;
            }
            return foundUser;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public UserDto findByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No such user: " + username)));
    }


}
