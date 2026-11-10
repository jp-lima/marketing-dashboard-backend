package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class UserCreateResponseDTO {

    // Getters e Setters
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime created_at;

    public UserCreateResponseDTO() {
    }

}
