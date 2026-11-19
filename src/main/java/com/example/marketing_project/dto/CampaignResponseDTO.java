package com.example.marketing_project.dto;

import com.example.marketing_project.enums.Platform;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CampaignResponseDTO {

    private UUID id;
    private String name;
    private UUID userId;
    private UUID productId;
    private Platform platform;
    private BigDecimal investment;
    private BigDecimal remainingInvestment;
    private BigDecimal campaignSpend;
    private Integer leadsCount;
    private BigDecimal leadPrice;
    private Integer segmentAgeMin;
    private Integer segmentAgeMax;
    private String segmentGender;
    private String segmentLocation;
    private List<String> interests;
    private Boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
