package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TopProductDTO {

    private String product;
    private Long quantity;
    private BigDecimal revenue;

    public TopProductDTO(String product, Long quantity, BigDecimal revenue) {
        this.product = product;
        this.quantity = quantity;
        this.revenue = revenue;
    }

}
