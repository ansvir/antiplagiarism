package com.example.antiplagiarism.repository.db;

import com.example.antiplagiarism.repository.entity.TextTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextTestRepository extends JpaRepository<TextTest, Long> {
}
