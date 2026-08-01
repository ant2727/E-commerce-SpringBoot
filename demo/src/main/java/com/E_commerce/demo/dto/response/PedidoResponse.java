package com.E_commerce.demo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PedidoResponse {

    private Long id;
    private Long clienteId;
    private String status;
    private BigDecimal total;
    private LocalDateTime dataCriacao;
    private List<ItemPedidoResponse> itens;
}
