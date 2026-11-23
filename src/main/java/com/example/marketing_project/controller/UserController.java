package com.example.marketing_project.controller;

import com.example.marketing_project.dto.MeResponseDTO;
import com.example.marketing_project.dto.MeUpdateRequestDTO;
import com.example.marketing_project.dto.PasswordUpdateRequestDTO;
import com.example.marketing_project.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints de usuário")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário logado")
    public ResponseEntity<MeResponseDTO> me(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(authService.getCurrentUser(userId));
    }

    @PutMapping("/me")
    @Operation(summary = "Atualizar dados do usuário logado")
    public ResponseEntity<MeResponseDTO> updateMe(
            Authentication authentication,
            @Valid @RequestBody MeUpdateRequestDTO dto) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(authService.updateCurrentUser(userId, dto));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Atualizar senha do usuário logado")
    public ResponseEntity<Void> updatePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordUpdateRequestDTO dto) {
        UUID userId = UUID.fromString(authentication.getName());
        authService.updatePassword(userId, dto);
        return ResponseEntity.noContent().build();
    }
}
