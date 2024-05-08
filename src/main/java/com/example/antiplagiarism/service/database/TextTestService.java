package com.example.antiplagiarism.service.database;

import com.example.antiplagiarism.mapper.ProfileMapper;
import com.example.antiplagiarism.mapper.TextTestMapper;
import com.example.antiplagiarism.repository.db.ProfileRepository;
import com.example.antiplagiarism.repository.db.TextTestRepository;
import com.example.antiplagiarism.repository.db.UserRepository;
import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.service.IService;
import com.example.antiplagiarism.service.model.ProfileDto;
import com.example.antiplagiarism.service.model.TextTestDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
@Transactional
public class TextTestService implements IService<TextTestDto, Long> {

    private final TextTestRepository textTestRepository;
    private final TextTestMapper textTestMapper;

    @Override
    public List<TextTestDto> findAll() {
        return textTestRepository.findAll()
                .stream()
                .map(textTestMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public TextTestDto save(TextTestDto entity) {
        return textTestMapper.toDto(textTestRepository.save(textTestMapper.toEntity(entity)));
    }

    @Override
    public void updateAll(List<TextTestDto> entities) {
        List<TextTest> textTests = entities
                .stream()
                .map(textTestMapper::toEntity)
                .collect(Collectors.toList());
        textTestRepository.saveAll(textTests);
    }

    @Override
    public long count() {
        return textTestRepository.count();
    }

}
