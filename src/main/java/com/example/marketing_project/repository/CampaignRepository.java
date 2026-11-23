package com.example.marketing_project.repository;

import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
    List<CampaignEntity> findByUserIdAndPlatform(UUID userId, Platform platform);

    List<CampaignEntity> findByUserId(UUID userId);
}
