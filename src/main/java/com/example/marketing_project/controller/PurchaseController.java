package com.example.marketing_project.controller;

import com.example.marketing_project.dto.PurchaseResponseDTO;
import com.example.marketing_project.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@Tag(name = "Vendas", description = "Consulta de vendas")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    @Operation(summary = "Listar vendas")
    public ResponseEntity<List<PurchaseResponseDTO>> findAll() {
        return ResponseEntity.ok(purchaseService.findAllPurchases());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID")
    public ResponseEntity<PurchaseResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseService.findPurchaseById(id));
    }
}
