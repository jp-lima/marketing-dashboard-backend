package com.example.marketing_project.mapper;

import com.example.marketing_project.dto.CampaignRequestDTO;
import com.example.marketing_project.dto.CampaignResponseDTO;
import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CampaignMapper {

    public CampaignEntity toEntity(CampaignRequestDTO dto, UserEntity user) {
        CampaignEntity campaign = new CampaignEntity();
        campaign.setName(dto.getName());
        campaign.setUser(user);
        campaign.setProductId(dto.getProductId());
        campaign.setPlatform(dto.getPlatform());
        campaign.setInvestment(dto.getInvestment());
        campaign.setRemainingInvestment(dto.getInvestment());
        campaign.setCampaignSpend(BigDecimal.ZERO);
        campaign.setLeadsCount(0);
        campaign.setLeadPrice(BigDecimal.ZERO);
        campaign.setSegmentAgeMin(dto.getSegmentAgeMin());
        campaign.setSegmentAgeMax(dto.getSegmentAgeMax());
        campaign.setSegmentGender(dto.getSegmentGender());
        campaign.setSegmentLocation(dto.getSegmentLocation());
        campaign.setInterests(dto.getInterests());
        campaign.setActive(dto.getActive());
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        return campaign;
    }

    public CampaignResponseDTO toResponseDTO(CampaignEntity entity) {
        CampaignResponseDTO dto = new CampaignResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUserId(entity.getUser().getId());
        dto.setProductId(entity.getProductId());
        dto.setPlatform(entity.getPlatform());
        dto.setInvestment(entity.getInvestment());
        dto.setRemainingInvestment(entity.getRemainingInvestment());
        dto.setCampaignSpend(entity.getCampaignSpend());
        dto.setLeadsCount(entity.getLeadsCount());
        dto.setLeadPrice(entity.getLeadPrice());
        dto.setSegmentAgeMin(entity.getSegmentAgeMin());
        dto.setSegmentAgeMax(entity.getSegmentAgeMax());
        dto.setSegmentGender(entity.getSegmentGender());
        dto.setSegmentLocation(entity.getSegmentLocation());
        dto.setInterests(entity.getInterests());
        dto.setActive(entity.getActive());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
