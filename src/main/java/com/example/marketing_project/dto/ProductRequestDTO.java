package com.example.marketing_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ProductRequestDTO {
    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "o preço do produto é obrigatório")
    @Positive(message = "o preço deve ser positivo")
    private Integer price;

    @NotNull(message = "o status do produto é obrigatório")
    private boolean active;

}
