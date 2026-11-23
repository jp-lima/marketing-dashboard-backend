package com.example.marketing_project.repository;

import com.example.marketing_project.entity.PurchaseItemEntity;
import com.example.marketing_project.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, UUID> {

    @Query("""
            SELECT pi.productId, SUM(pi.quantity), SUM(pi.subtotal)
            FROM PurchaseItemEntity pi
            WHERE pi.purchase.campaign.user.id = :userId
              AND pi.purchase.status = :status
              AND (:platform IS NULL OR pi.purchase.campaign.platform = :platform)
              AND pi.purchase.purchasedAt BETWEEN :start AND :end
            GROUP BY pi.productId
            """)
    List<Object[]> sumQuantityAndRevenueByProduct(
            @Param("userId") UUID userId,
            @Param("status") PurchaseStatus status,
            @Param("platform") Object platform,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
