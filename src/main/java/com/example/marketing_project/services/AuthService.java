package com.example.marketing_project.services;

import com.example.marketing_project.dto.LoginRequestDTO;
import com.example.marketing_project.dto.LoginResponseDTO;
import com.example.marketing_project.dto.MeResponseDTO;
import com.example.marketing_project.dto.MeUpdateRequestDTO;
import com.example.marketing_project.dto.PasswordUpdateRequestDTO;
import com.example.marketing_project.dto.UserCreateRequestDTO;
import com.example.marketing_project.dto.UserCreateResponseDTO;
import com.example.marketing_project.entity.UserEntity;
import com.example.marketing_project.mapper.UserMapper;
import com.example.marketing_project.repository.UserRepository;
import com.example.marketing_project.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserCreateResponseDTO createUser(UserCreateRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(token, user.getId().toString(), user.getName(), user.getEmail());
    }

    public MeResponseDTO getCurrentUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return userMapper.toMeResponseDTO(user);
    }

    public MeResponseDTO updateCurrentUser(UUID userId, MeUpdateRequestDTO dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        user.setName(dto.getName());

        return userMapper.toMeResponseDTO(userRepository.save(user));
    }

    public void updatePassword(UUID userId, PasswordUpdateRequestDTO dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getCurrent_password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }

        if (!dto.getNew_password().equals(dto.getConfirm_new_password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha e confirmação não conferem");
        }

        user.setPassword(passwordEncoder.encode(dto.getNew_password()));
        userRepository.save(user);
    }

}
