package com.example.marketing_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class WebhookLeadResponseDTO {

    private String message;

    @JsonProperty("campaign_id")
    private UUID campaignId;

    @JsonProperty("leads_count")
    private Integer leadsCount;

    @JsonProperty("campaign_spend")
    private BigDecimal campaignSpend;

    @JsonProperty("remaining_investment")
    private BigDecimal remainingInvestment;

    @JsonProperty("lead_price")
    private BigDecimal leadPrice;

    public WebhookLeadResponseDTO() {
    }

    public WebhookLeadResponseDTO(String message,
                                  UUID campaignId,
                                  Integer leadsCount,
                                  BigDecimal campaignSpend,
                                  BigDecimal remainingInvestment,
                                  BigDecimal leadPrice) {
        this.message = message;
        this.campaignId = campaignId;
        this.leadsCount = leadsCount;
        this.campaignSpend = campaignSpend;
        this.remainingInvestment = remainingInvestment;
        this.leadPrice = leadPrice;
    }
}
