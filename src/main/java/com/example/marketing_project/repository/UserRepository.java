package com.example.marketing_project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketing_project.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
