package com.example.marketing_project.repository;

import com.example.marketing_project.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, UUID> {
}
