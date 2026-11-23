package com.example.marketing_project.repository;

import com.example.marketing_project.entity.PurchaseEntity;
import com.example.marketing_project.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, UUID> {

    @Query("SELECT COUNT(p) FROM PurchaseEntity p WHERE p.campaign.id = :campaignId AND p.status = :status")
    long countByCampaignIdAndStatus(@Param("campaignId") UUID campaignId, @Param("status") PurchaseStatus status);

    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM PurchaseEntity p WHERE p.campaign.id = :campaignId AND p.status = :status")
    BigDecimal sumTotalAmountByCampaignIdAndStatus(@Param("campaignId") UUID campaignId, @Param("status") PurchaseStatus status);
}
