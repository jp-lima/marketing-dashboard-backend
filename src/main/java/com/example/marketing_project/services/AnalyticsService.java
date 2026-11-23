package com.example.marketing_project.services;

import com.example.marketing_project.dto.AnalyticsResponseDTO;
import com.example.marketing_project.dto.CampaignComparisonDTO;
import com.example.marketing_project.dto.SalesByPlatformDTO;
import com.example.marketing_project.dto.SalesOverTimeDTO;
import com.example.marketing_project.dto.TopProductDTO;
import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.entity.PurchaseEntity;
import com.example.marketing_project.enums.Platform;
import com.example.marketing_project.enums.PurchaseStatus;
import com.example.marketing_project.repository.CampaignRepository;
import com.example.marketing_project.repository.ProductRepository;
import com.example.marketing_project.repository.PurchaseItemRepository;
import com.example.marketing_project.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final CampaignRepository campaignRepository;
    private final ProductRepository productRepository;

    public AnalyticsService(PurchaseRepository purchaseRepository,
                            PurchaseItemRepository purchaseItemRepository,
                            CampaignRepository campaignRepository,
                            ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.campaignRepository = campaignRepository;
        this.productRepository = productRepository;
    }

    public AnalyticsResponseDTO getAnalytics(UUID userId, LocalDate startDate, LocalDate endDate, Platform platform) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data inicial deve ser anterior ou igual à data final");
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<PurchaseEntity> purchases = purchaseRepository.findPaidPurchasesByUserAndFilters(
                userId, PurchaseStatus.PAID, platform, start, end);

        AnalyticsResponseDTO response = new AnalyticsResponseDTO();
        response.setSales_over_time(buildSalesOverTime(purchases));
        response.setCampaigns_comparison(buildCampaignsComparison(purchases));
        response.setSales_by_platform(buildSalesByPlatform(purchases));
        response.setTop_products(buildTopProducts(userId, platform, start, end));
        return response;
    }

    private List<SalesOverTimeDTO> buildSalesOverTime(List<PurchaseEntity> purchases) {
        Map<LocalDate, BigDecimal> grouped = purchases.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPurchasedAt().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, PurchaseEntity::getTotalAmount, BigDecimal::add)
                ));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new SalesOverTimeDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<CampaignComparisonDTO> buildCampaignsComparison(List<PurchaseEntity> purchases) {
        Map<UUID, BigDecimal> revenueByCampaign = purchases.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCampaign().getId(),
                        Collectors.reducing(BigDecimal.ZERO, PurchaseEntity::getTotalAmount, BigDecimal::add)
                ));

        List<UUID> campaignIds = revenueByCampaign.keySet().stream().toList();
        Map<UUID, CampaignEntity> campaigns = campaignRepository.findAllById(campaignIds).stream()
                .collect(Collectors.toMap(CampaignEntity::getId, c -> c));

        return revenueByCampaign.entrySet().stream()
                .map(e -> {
                    CampaignEntity campaign = campaigns.get(e.getKey());
                    if (campaign == null) {
                        return null;
                    }
                    return new CampaignComparisonDTO(
                            campaign.getName(),
                            e.getValue(),
                            campaign.getCampaignSpend() != null ? campaign.getCampaignSpend() : BigDecimal.ZERO
                    );
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(CampaignComparisonDTO::getRevenue).reversed())
                .collect(Collectors.toList());
    }

    private List<SalesByPlatformDTO> buildSalesByPlatform(List<PurchaseEntity> purchases) {
        Map<Platform, BigDecimal> grouped = purchases.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCampaign().getPlatform(),
                        Collectors.reducing(BigDecimal.ZERO, PurchaseEntity::getTotalAmount, BigDecimal::add)
                ));

        return grouped.entrySet().stream()
                .map(e -> new SalesByPlatformDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<TopProductDTO> buildTopProducts(UUID userId, Platform platform, LocalDateTime start, LocalDateTime end) {
        List<Object[]> rows = purchaseItemRepository.sumQuantityAndRevenueByProduct(
                userId, PurchaseStatus.PAID, platform, start, end);

        Map<UUID, String> productNames = productRepository.findAll().stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p.getName()));

        return rows.stream()
                .map(row -> {
                    UUID productId = (UUID) row[0];
                    Long quantity = ((Number) row[1]).longValue();
                    BigDecimal revenue = (BigDecimal) row[2];
                    String name = productNames.getOrDefault(productId, "Produto desconhecido");
                    return new TopProductDTO(name, quantity, revenue);
                })
                .sorted(Comparator.comparing(TopProductDTO::getRevenue).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
