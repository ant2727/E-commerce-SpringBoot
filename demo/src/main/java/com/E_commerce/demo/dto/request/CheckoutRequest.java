package com.E_commerce.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutRequest {

    @NotNull(message = "O cliente é obrigatório.")
    private Long clienteId;
}
