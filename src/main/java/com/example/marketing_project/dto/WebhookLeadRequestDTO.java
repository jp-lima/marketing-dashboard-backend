package com.example.marketing_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class WebhookLeadRequestDTO {

    @JsonProperty("campaign_id")
    private UUID campaignId;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("lead_cost")
    private BigDecimal leadCost;

    @JsonProperty("created_at")
    private Instant createdAt;
}
