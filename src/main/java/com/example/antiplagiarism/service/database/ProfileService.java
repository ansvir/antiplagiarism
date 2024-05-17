package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.ProfileMapper;
import com.example.antiplagiarism.repository.db.ProfileRepository;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
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

    @Override
    public List<ProfileDto> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProfileDto findById(Long id) {
        return profileRepository.findById(id)
                .map(profileMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No such profile for id: " + id));
    }

    public ProfileDto findByUsername(String username) {
        return profileRepository.findByUsername(username)
                .map(profileMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No such profile for username: " + username));
    }

    @Override
    public ProfileDto save(ProfileDto entity) {
        return profileMapper.toDto(
                profileRepository.save(profileMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<ProfileDto> entities) {
        throw new NotYetImplementedException("Not yet implemented!");
    }

    @Override
    public long count() {
        return profileRepository.count();
    }

}
