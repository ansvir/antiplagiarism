package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.ProfileMapper;
import com.example.antiplagiarism.repository.db.ProfileRepository;
import com.example.antiplagiarism.repository.db.UserRepository;
import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService implements IService<ProfileDto, Long> {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    @Override
    public List<ProfileDto> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProfileDto save(ProfileDto entity) {
        return profileMapper.toDto(profileRepository.save(profileMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<ProfileDto> entities) {
        List<Profile> profiles = entities
                .stream()
                .map(profileMapper::toEntity)
                .collect(Collectors.toList());
        profileRepository.saveAll(profiles);
    }

    @Override
    public long count() {
        return profileRepository.count();
    }

}
