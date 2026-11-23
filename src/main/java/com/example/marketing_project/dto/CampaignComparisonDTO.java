package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CampaignComparisonDTO {

    private String campaign;
    private BigDecimal revenue;
    private BigDecimal spend;

    public CampaignComparisonDTO(String campaign, BigDecimal revenue, BigDecimal spend) {
        this.campaign = campaign;
        this.revenue = revenue;
        this.spend = spend;
    }

}
