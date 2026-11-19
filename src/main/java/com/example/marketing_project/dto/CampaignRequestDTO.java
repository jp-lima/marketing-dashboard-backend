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
public class CampaignRequestDTO {

    @NotBlank(message = "O nome da campanha é obrigatório")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    private String name;

    @NotNull(message = "O ID do usuário é obrigatório")
    private UUID userId;

    @NotNull(message = "O ID do produto é obrigatório")
    private UUID productId;

    @NotNull(message = "A plataforma é obrigatória")
    private Platform platform;

    @NotNull(message = "O investimento é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "O investimento não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "O investimento deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal investment;

    @NotNull(message = "A idade mínima do segmento é obrigatória")
    @Min(value = 0, message = "A idade mínima deve ser no mínimo 0")
    @Max(value = 120, message = "A idade mínima deve ser no máximo 120")
    private Integer segmentAgeMin;

    @NotNull(message = "A idade máxima do segmento é obrigatória")
    @Min(value = 0, message = "A idade máxima deve ser no mínimo 0")
    @Max(value = 120, message = "A idade máxima deve ser no máximo 120")
    private Integer segmentAgeMax;

    @NotBlank(message = "O gênero do segmento é obrigatório")
    @Size(max = 20, message = "O gênero deve ter no máximo 20 caracteres")
    private String segmentGender;

    @NotBlank(message = "A localização do segmento é obrigatória")
    @Size(max = 255, message = "A localização deve ter no máximo 255 caracteres")
    private String segmentLocation;

    private List<@NotBlank(message = "O interesse não pode estar em branco") @Size(max = 100, message = "O interesse deve ter no máximo 100 caracteres") String> interests;

    @NotNull(message = "O status ativo é obrigatório")
    private Boolean active;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate startDate;

    @NotNull(message = "A data de término é obrigatória")
    private LocalDate endDate;
}
