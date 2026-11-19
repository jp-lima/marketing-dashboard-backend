package com.example.marketing_project.dto;

import com.example.marketing_project.enums.PurchaseStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PurchaseResponseDTO {

    private UUID id;
    private UUID customerId;
    private String customerName;
    private UUID campaignId;
    private String campaignName;
    private BigDecimal totalAmount;
    private PurchaseStatus status;
    private LocalDateTime purchasedAt;
    private LocalDateTime createdAt;
}
