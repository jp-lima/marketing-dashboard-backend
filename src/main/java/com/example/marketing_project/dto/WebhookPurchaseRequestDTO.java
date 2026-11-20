package com.example.marketing_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class WebhookPurchaseRequestDTO {

    @JsonProperty("campaign_id")
    private UUID campaignId;

    @JsonProperty("external_id")
    private String externalId;

    private String platform;

    private WebhookPurchaseCustomerRequestDTO customer;

    private List<WebhookPurchaseItemRequestDTO> items;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("purchased_at")
    private Instant purchasedAt;
}
