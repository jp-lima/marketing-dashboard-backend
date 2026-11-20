package com.example.marketing_project.services;

import com.example.marketing_project.dto.WebhookLeadRequestDTO;
import com.example.marketing_project.dto.WebhookLeadResponseDTO;
import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.enums.Platform;
import com.example.marketing_project.repository.CampaignRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class WebhookLeadService {

    private final CampaignRepository campaignRepository;

    public WebhookLeadService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public WebhookLeadResponseDTO processLead(WebhookLeadRequestDTO dto) {
        validateRequest(dto);

        UUID campaignId = dto.getCampaignId();
        BigDecimal leadCost = dto.getLeadCost();

        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada"));

        Integer currentLeadsCount = campaign.getLeadsCount() != null ? campaign.getLeadsCount() : 0;
        BigDecimal currentCampaignSpend = campaign.getCampaignSpend() != null ? campaign.getCampaignSpend() : BigDecimal.ZERO;
        BigDecimal currentRemainingInvestment = campaign.getRemainingInvestment() != null
                ? campaign.getRemainingInvestment()
                : campaign.getInvestment();

        Integer updatedLeadsCount = currentLeadsCount + 1;
        BigDecimal updatedCampaignSpend = currentCampaignSpend.add(leadCost);
        BigDecimal updatedRemainingInvestment = currentRemainingInvestment.subtract(leadCost);
        BigDecimal leadPrice = updatedCampaignSpend
                .divide(BigDecimal.valueOf(updatedLeadsCount), 4, RoundingMode.HALF_UP);

        campaign.setLeadsCount(updatedLeadsCount);
        campaign.setCampaignSpend(updatedCampaignSpend);
        campaign.setRemainingInvestment(updatedRemainingInvestment);
        campaign.setLeadPrice(leadPrice);

        CampaignEntity updatedCampaign = campaignRepository.save(campaign);

        return new WebhookLeadResponseDTO(
                "Lead processado com sucesso",
                updatedCampaign.getId(),
                updatedCampaign.getLeadsCount(),
                updatedCampaign.getCampaignSpend(),
                updatedCampaign.getRemainingInvestment(),
                updatedCampaign.getLeadPrice()
        );
    }

    private void validateRequest(WebhookLeadRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getCampaignId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getLeadCost() == null || dto.getLeadCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getPlatform() == null || dto.getPlatform().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        try {
            Platform.valueOf(dto.getPlatform().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }
    }
}
