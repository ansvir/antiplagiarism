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
public class TextTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12000)
    private String textOne;

    @Column(nullable = false, length = 12000)
    private String textTwo;

    @Column(nullable = false)
    private BigDecimal plagiatResult;

}
