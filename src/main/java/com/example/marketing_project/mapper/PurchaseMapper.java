package com.example.marketing_project.mapper;

import com.example.marketing_project.dto.PurchaseResponseDTO;
import com.example.marketing_project.entity.PurchaseEntity;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    public PurchaseResponseDTO toResponseDTO(PurchaseEntity entity) {
        PurchaseResponseDTO dto = new PurchaseResponseDTO();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setCustomerName(entity.getCustomer().getName());
        dto.setCampaignId(entity.getCampaign().getId());
        dto.setCampaignName(entity.getCampaign().getName());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setStatus(entity.getStatus());
        dto.setPurchasedAt(entity.getPurchasedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
