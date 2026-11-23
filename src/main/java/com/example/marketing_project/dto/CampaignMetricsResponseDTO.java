package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CampaignMetricsResponseDTO {

    private UUID campaign_id;
    private BigDecimal revenue;
    private BigDecimal spend;
    private BigDecimal profit;
    private BigDecimal roas;
    private BigDecimal roi;
    private BigDecimal cac;
    private BigDecimal ltv;
    private Integer leads_count;
    private BigDecimal lead_price;
    private BigDecimal conversion_rate;

    public CampaignMetricsResponseDTO() {
    }

}
