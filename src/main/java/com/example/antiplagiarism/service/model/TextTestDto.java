package com.example.antiplagiarism.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextTestDto implements Comparable<TextTestDto> {

    private Long id;
    private String textOne;
    private String textTwo;
    private BigDecimal plagiatResult;
    private List<TriadaDto> triads;

    @Override
    public int compareTo(TextTestDto o) {
        if (this.id == null || o.id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return this.id.compareTo(o.id);
    }

}
