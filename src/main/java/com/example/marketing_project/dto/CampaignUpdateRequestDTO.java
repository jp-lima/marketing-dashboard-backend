package com.example.marketing_project.dto;

import com.example.marketing_project.enums.Platform;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CampaignUpdateRequestDTO {

    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    private String name;

    private UUID userId;

    private UUID productId;

    private Platform platform;

    @DecimalMin(value = "0.00", inclusive = true, message = "O investimento não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "O investimento deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal investment;

    @Min(value = 0, message = "A idade mínima deve ser no mínimo 0")
    @Max(value = 120, message = "A idade mínima deve ser no máximo 120")
    private Integer segmentAgeMin;

    @Min(value = 0, message = "A idade máxima deve ser no mínimo 0")
    @Max(value = 120, message = "A idade máxima deve ser no máximo 120")
    private Integer segmentAgeMax;

    @Size(max = 20, message = "O gênero deve ter no máximo 20 caracteres")
    private String segmentGender;

    @Size(max = 255, message = "A localização deve ter no máximo 255 caracteres")
    private String segmentLocation;

    private List<@NotBlank(message = "O interesse não pode estar em branco") @Size(max = 100, message = "O interesse deve ter no máximo 100 caracteres") String> interests;

    private Boolean active;

    private LocalDate startDate;

    private LocalDate endDate;
}
