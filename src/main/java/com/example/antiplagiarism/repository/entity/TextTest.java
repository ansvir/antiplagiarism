package com.example.antiplagiarism.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "text_test")
public class TextTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String textOne;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String textTwo;

    @Column(nullable = false)
    private BigDecimal plagiatResult;

}
