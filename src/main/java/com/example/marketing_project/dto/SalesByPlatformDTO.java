package com.example.marketing_project.dto;

import com.example.marketing_project.enums.Platform;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SalesByPlatformDTO {

    private Platform platform;
    private BigDecimal value;

    public SalesByPlatformDTO(Platform platform, BigDecimal value) {
        this.platform = platform;
        this.value = value;
    }

}
