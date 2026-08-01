package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.dto.response.ItemCarrinhoResponse;
import com.E_commerce.demo.entity.Carrinho;
import com.E_commerce.demo.entity.ItemCarrinho;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarrinhoMapper {

    public CarrinhoResponse toResponse(Carrinho carrinho) {
        List<ItemCarrinhoResponse> itens = carrinho.getItens()
                .stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal total = itens.stream()
                .map(ItemCarrinhoResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CarrinhoResponse response = new CarrinhoResponse();
        response.setId(carrinho.getId());
        response.setClienteId(carrinho.getCliente().getId());
        response.setItens(itens);
        response.setTotal(total);
        return response;
    }

    private ItemCarrinhoResponse toItemResponse(ItemCarrinho item) {
        ItemCarrinhoResponse dto = new ItemCarrinhoResponse();
        dto.setProdutoId(item.getProduto().getId());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(
                item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade()))
        );
        return dto;
    }
}
