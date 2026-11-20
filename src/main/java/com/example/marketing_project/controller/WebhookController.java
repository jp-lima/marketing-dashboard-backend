package com.example.marketing_project.controller;

import com.example.marketing_project.dto.WebhookLeadRequestDTO;
import com.example.marketing_project.dto.WebhookLeadResponseDTO;
import com.example.marketing_project.dto.WebhookPurchaseRequestDTO;
import com.example.marketing_project.services.WebhookLeadService;
import com.example.marketing_project.services.WebhookPurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@Tag(name = "Webhook", description = "Receber informações de leads das plataformas de vendas")
public class WebhookController {

    private final WebhookLeadService webhookLeadService;
    private final WebhookPurchaseService webhookPurchaseService;

    public WebhookController(WebhookLeadService webhookLeadService,
                             WebhookPurchaseService webhookPurchaseService) {
        this.webhookLeadService = webhookLeadService;
        this.webhookPurchaseService = webhookPurchaseService;
    }

    @PostMapping("/lead")
    @Operation(summary = "Receber lead de plataforma externa")
    public ResponseEntity<WebhookLeadResponseDTO> receiveLead(@RequestBody WebhookLeadRequestDTO dto) {
        WebhookLeadResponseDTO response = webhookLeadService.processLead(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/purchase")
    @Operation(summary = "Receber compra de plataforma externa")
    public ResponseEntity<Void> receivePurchase(@RequestBody WebhookPurchaseRequestDTO dto) {
        webhookPurchaseService.processPurchase(dto);
        return ResponseEntity.noContent().build();
    }
}
