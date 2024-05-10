package com.example.antiplagiarism.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "app_user")
public class User {

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<TextTest> textTests;

    @Column(nullable = false)
    private boolean enabled;

}
