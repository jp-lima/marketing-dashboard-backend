package com.example.marketing_project.controller;

import com.example.marketing_project.dto.AnalyticsResponseDTO;
import com.example.marketing_project.enums.Platform;
import com.example.marketing_project.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Métricas e gráficos do dashboard")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    @Operation(summary = "Retorna métricas e dados dos gráficos")
    public ResponseEntity<AnalyticsResponseDTO> getAnalytics(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) Platform platform) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(analyticsService.getAnalytics(userId, start_date, end_date, platform));
    }
}
