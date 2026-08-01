package com.E_commerce.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CarrinhoResponse {

    private Long id;
    private Long clienteId;
    private List<ItemCarrinhoResponse> itens;
    private BigDecimal total;

    public CarrinhoResponse() {
    }
}
