package com.example.marketing_project.controller;

import com.example.marketing_project.dto.CampaignRequestDTO;
import com.example.marketing_project.dto.CampaignResponseDTO;
import com.example.marketing_project.dto.CampaignUpdateRequestDTO;
import com.example.marketing_project.services.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/campaign")
@Tag(name = "Campanhas", description = "Criar e alterar campanhas de marketing")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("")
    @Operation(summary = "Criar campanha")
    public ResponseEntity<CampaignResponseDTO> createCampaign(@Valid @RequestBody CampaignRequestDTO dto) {
        CampaignResponseDTO response = campaignService.createCampaign(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    @Operation(summary = "Buscar todas as campanhas")
    public ResponseEntity<List<CampaignResponseDTO>> getAllCampaign() {
        return ResponseEntity.ok(campaignService.findAllCampaigns());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<CampaignResponseDTO> getCampaignById(@PathVariable UUID id) {
        return ResponseEntity.ok(campaignService.findCampaignById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir campanha")
    public ResponseEntity<Void> deleteCampaign(@PathVariable UUID id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente campanha")
    public ResponseEntity<CampaignResponseDTO> updateCampaign(
            @PathVariable UUID id,
            @Valid @RequestBody CampaignUpdateRequestDTO dto) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, dto));
    }
}
