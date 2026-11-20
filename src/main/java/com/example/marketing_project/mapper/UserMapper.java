package com.example.marketing_project.mapper;

import com.example.marketing_project.dto.MeResponseDTO;
import com.example.marketing_project.dto.UserCreateRequestDTO;
import com.example.marketing_project.dto.UserCreateResponseDTO;
import com.example.marketing_project.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserCreateRequestDTO dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());   // ⚠️ depois precisa criptografar!
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserCreateResponseDTO toResponseDTO(UserEntity entity) {
        UserCreateResponseDTO dto = new UserCreateResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreated_at(entity.getCreated_at());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public MeResponseDTO toMeResponseDTO(UserEntity entity) {
        MeResponseDTO dto = new MeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setCreated_at(entity.getCreated_at());
        return dto;
    }
}
