package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnalyticsResponseDTO {

    private List<SalesOverTimeDTO> sales_over_time;
    private List<CampaignComparisonDTO> campaigns_comparison;
    private List<SalesByPlatformDTO> sales_by_platform;
    private List<TopProductDTO> top_products;

}
