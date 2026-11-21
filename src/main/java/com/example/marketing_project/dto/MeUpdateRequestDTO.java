package com.example.marketing_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeUpdateRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

}
