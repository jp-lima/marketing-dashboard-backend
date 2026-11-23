package com.example.marketing_project.repository;

import com.example.marketing_project.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    @Query("SELECT COUNT(c) FROM CustomerEntity c WHERE c.sourceCampaign.id = :campaignId")
    long countBySourceCampaignId(@Param("campaignId") UUID campaignId);
}
