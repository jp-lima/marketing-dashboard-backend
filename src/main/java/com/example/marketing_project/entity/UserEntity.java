package com.example.marketing_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    public UserEntity(UUID id, String name, String cpf, String password,
       LocalDate birth_date, String gender, String role, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.created_at = created_at;
    }
    public UserEntity(){}

}
