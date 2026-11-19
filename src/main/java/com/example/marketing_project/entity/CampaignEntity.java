package com.example.marketing_project.entity;

import com.example.marketing_project.enums.Platform;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "campaigns")
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Platform platform;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal investment;

    @Column(name = "remaining_investment", precision = 10, scale = 2)
    private BigDecimal remainingInvestment;

    @Column(name = "campaign_spend", precision = 10, scale = 2)
    private BigDecimal campaignSpend;

    @Column(name = "leads_count")
    private Integer leadsCount;

    @Column(name = "lead_price", precision = 10, scale = 4)
    private BigDecimal leadPrice;

    @Column(name = "segment_age_min", nullable = false)
    private Integer segmentAgeMin;

    @Column(name = "segment_age_max", nullable = false)
    private Integer segmentAgeMax;

    @Column(name = "segment_gender", nullable = false, length = 20)
    private String segmentGender;

    @Column(name = "segment_location", nullable = false, length = 255)
    private String segmentLocation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> interests;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
