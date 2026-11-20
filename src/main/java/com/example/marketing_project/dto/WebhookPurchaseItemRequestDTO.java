package com.example.marketing_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class WebhookPurchaseItemRequestDTO {

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    private Integer quantity;
}
