package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.response.ItemPedidoResponse;
import com.E_commerce.demo.dto.response.PedidoResponse;
import com.E_commerce.demo.entity.ItemPedido;
import com.E_commerce.demo.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setClienteId(pedido.getCliente().getId());
        response.setStatus(pedido.getStatus().name());
        response.setTotal(pedido.getTotal());
        response.setDataCriacao(pedido.getDataCriacao());
        response.setItens(
                pedido.getItens().stream()
                        .map(this::toItemResponse)
                        .toList()
        );
        return response;
    }

    public List<PedidoResponse> toResponseList(List<Pedido> pedidos) {
        return pedidos.stream().map(this::toResponse).toList();
    }

    private ItemPedidoResponse toItemResponse(ItemPedido item) {
        ItemPedidoResponse dto = new ItemPedidoResponse();
        dto.setProdutoId(item.getProduto().getId());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
