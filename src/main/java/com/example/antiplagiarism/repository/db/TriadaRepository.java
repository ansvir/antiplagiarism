package com.example.antiplagiarism.repository.db;

import com.example.antiplagiarism.repository.entity.Triada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriadaRepository extends JpaRepository<Triada, Long> {
}
