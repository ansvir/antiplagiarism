package com.example.antiplagiarism.mapper;

import com.example.antiplagiarism.repository.entity.TextTest;
import com.example.antiplagiarism.repository.entity.Triada;
import com.example.antiplagiarism.service.model.TextTestDto;
import com.example.antiplagiarism.service.model.TriadaDto;
import org.springframework.stereotype.Component;

@Component
public class TriadaMapper {

    public TriadaDto toDto(Triada triada) {
        return new TriadaDto(triada.getId(), triada.getValue());
    }

    public Triada toEntity(TriadaDto triadaDto) {
        return new Triada(triadaDto.getId(), triadaDto.getValue());
    }

}
