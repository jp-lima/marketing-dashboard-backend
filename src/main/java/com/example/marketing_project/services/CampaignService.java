package com.example.marketing_project.services;

import com.example.marketing_project.dto.CampaignMetricsResponseDTO;
import com.example.marketing_project.dto.CampaignRequestDTO;
import com.example.marketing_project.dto.CampaignResponseDTO;
import com.example.marketing_project.dto.CampaignUpdateRequestDTO;
import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.entity.UserEntity;
import com.example.marketing_project.enums.PurchaseStatus;
import com.example.marketing_project.mapper.CampaignMapper;
import com.example.marketing_project.repository.CampaignRepository;
import com.example.marketing_project.repository.CustomerRepository;
import com.example.marketing_project.repository.PurchaseRepository;
import com.example.marketing_project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;

    public CampaignService(CampaignRepository campaignRepository,
                           CampaignMapper campaignMapper,
                           UserRepository userRepository,
                           PurchaseRepository purchaseRepository,
                           CustomerRepository customerRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
    }

    public CampaignResponseDTO createCampaign(CampaignRequestDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de início deve ser anterior ou igual à data de término");
        }

        CampaignEntity campaign = campaignMapper.toEntity(dto, user);
        CampaignEntity savedCampaign = campaignRepository.save(campaign);

        return campaignMapper.toResponseDTO(savedCampaign);
    }

    public List<CampaignResponseDTO> findAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();
    }

    public CampaignResponseDTO findCampaignById(UUID id) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada"));
        return campaignMapper.toResponseDTO(campaign);
    }

    public CampaignResponseDTO updateCampaign(UUID id, CampaignUpdateRequestDTO dto) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada"));

        if (dto.getUserId() != null) {
            UserEntity user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
            campaign.setUser(user);
        }

        LocalDate startDate = dto.getStartDate() != null ? dto.getStartDate() : campaign.getStartDate();
        LocalDate endDate = dto.getEndDate() != null ? dto.getEndDate() : campaign.getEndDate();
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de início deve ser anterior ou igual à data de término");
        }

        if (dto.getName() != null) campaign.setName(dto.getName());
        if (dto.getProductId() != null) campaign.setProductId(dto.getProductId());
        if (dto.getPlatform() != null) campaign.setPlatform(dto.getPlatform());
        if (dto.getInvestment() != null) campaign.setInvestment(dto.getInvestment());
        if (dto.getSegmentAgeMin() != null) campaign.setSegmentAgeMin(dto.getSegmentAgeMin());
        if (dto.getSegmentAgeMax() != null) campaign.setSegmentAgeMax(dto.getSegmentAgeMax());
        if (dto.getSegmentGender() != null) campaign.setSegmentGender(dto.getSegmentGender());
        if (dto.getSegmentLocation() != null) campaign.setSegmentLocation(dto.getSegmentLocation());
        if (dto.getInterests() != null) campaign.setInterests(dto.getInterests());
        if (dto.getActive() != null) campaign.setActive(dto.getActive());
        if (dto.getStartDate() != null) campaign.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) campaign.setEndDate(dto.getEndDate());

        CampaignEntity updatedCampaign = campaignRepository.save(campaign);
        return campaignMapper.toResponseDTO(updatedCampaign);
    }

    public void deleteCampaign(UUID id) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada"));
        campaignRepository.delete(campaign);
    }

    public CampaignMetricsResponseDTO getCampaignMetrics(UUID campaignId, UUID userId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada"));

        if (!campaign.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha não encontrada");
        }

        BigDecimal revenue = purchaseRepository.sumTotalAmountByCampaignIdAndStatus(campaignId, PurchaseStatus.PAID);
        long purchasesCount = purchaseRepository.countByCampaignIdAndStatus(campaignId, PurchaseStatus.PAID);
        long customersCount = customerRepository.countBySourceCampaignId(campaignId);
        BigDecimal spend = campaign.getCampaignSpend() != null ? campaign.getCampaignSpend() : BigDecimal.ZERO;

        BigDecimal profit = revenue.subtract(spend);
        BigDecimal roas = safeDivide(revenue, spend);
        BigDecimal roi = safeDivide(profit, spend);
        BigDecimal cac = safeDivide(spend, customersCount);
        BigDecimal ltv = safeDivide(revenue, customersCount);
        Integer leadsCount = campaign.getLeadsCount() != null ? campaign.getLeadsCount() : 0;
        BigDecimal leadPrice = safeDivide(spend, leadsCount);
        BigDecimal conversionRate = leadsCount == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(purchasesCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(leadsCount), 2, RoundingMode.HALF_UP);

        CampaignMetricsResponseDTO dto = new CampaignMetricsResponseDTO();
        dto.setCampaign_id(campaign.getId());
        dto.setRevenue(revenue);
        dto.setSpend(spend);
        dto.setProfit(profit);
        dto.setRoas(roas);
        dto.setRoi(roi);
        dto.setCac(cac);
        dto.setLtv(ltv);
        dto.setLeads_count(leadsCount);
        dto.setLead_price(leadPrice);
        dto.setConversion_rate(conversionRate);
        return dto;
    }

    private BigDecimal safeDivide(BigDecimal numerator, BigDecimal denominator) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal safeDivide(BigDecimal numerator, long denominator) {
        if (denominator == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
    }
}
