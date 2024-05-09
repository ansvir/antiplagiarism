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

    @Column(nullable = false)
    private String textOne;

    @Column(nullable = false)
    private String textTwo;

    @Column(nullable = false)
    private BigDecimal plagiatResult;

    @ManyToOne
    @Column(nullable = false)
    private Profile profileId;

}
