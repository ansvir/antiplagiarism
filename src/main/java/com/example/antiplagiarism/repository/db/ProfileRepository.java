package com.example.antiplagiarism.repository.db;

import com.example.antiplagiarism.repository.entity.Profile;
import com.example.antiplagiarism.repository.entity.TextTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE p.user.username LIKE :username")
    Optional<Profile> findByUsername(String username);
}
