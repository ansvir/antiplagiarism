package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.ProfileMapper;
import com.example.antiplagiarism.mapper.TextTestMapper;
import com.example.antiplagiarism.repository.db.ProfileRepository;
import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService implements IService<ProfileDto, Long> {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final TextTestMapper textTestMapper;

    @Override
    public List<ProfileDto> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profile -> profileMapper.toDto(profile,
                        profile.getTextTests().stream().map(textTestMapper::toDto)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ProfileDto save(ProfileDto entity) {
        Profile profile = profileRepository.findById(entity.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such profile by id: " + entity.getId()));
        Profile saved = profileRepository.save(profileMapper.toEntity(entity, profile.getTextTests()));
        return profileMapper.toDto(saved, profile.getTextTests()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void updateAll(List<ProfileDto> entities) {
        List<Profile> profiles = entities
                .stream()
                .map(profile -> profileMapper.toEntity(profile,
                        profile.getTextTestDtos().stream().map(textTestMapper::toEntity)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
        profileRepository.saveAll(profiles);
    }

    public ProfileDto findById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such profile with id: " + id));
        return profileMapper.toDto(profile, profile.getTextTests()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public long count() {
        return profileRepository.count();
    }

    public ProfileDto findByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No such profile by username: " + username));
        return profileMapper.toDto(profile, profile.getTextTests()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList()));
    }

}
