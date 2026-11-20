package com.example.marketing_project.repository;

import com.example.marketing_project.entity.PurchaseItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, UUID> {
}
