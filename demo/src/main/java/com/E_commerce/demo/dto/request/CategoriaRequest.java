package com.E_commerce.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoriaRequest {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
}
