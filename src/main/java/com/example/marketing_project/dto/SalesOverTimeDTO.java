package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SalesOverTimeDTO {

    private LocalDate date;
    private BigDecimal value;

    public SalesOverTimeDTO(LocalDate date, BigDecimal value) {
        this.date = date;
        this.value = value;
    }

}
