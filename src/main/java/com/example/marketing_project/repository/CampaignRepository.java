package com.example.marketing_project.repository;

import com.example.marketing_project.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
}
