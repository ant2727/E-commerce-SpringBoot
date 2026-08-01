package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.response.PedidoResponse;
import com.E_commerce.demo.entity.*;
import com.E_commerce.demo.enums.StatusPedido;
import com.E_commerce.demo.exception.*;
import com.E_commerce.demo.mapper.PedidoMapper;
import com.E_commerce.demo.repository.CarrinhoRepository;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.repository.ItemCarrinhoRepository;
import com.E_commerce.demo.repository.PedidoRepository;
import com.E_commerce.demo.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoResponse checkout(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNaoEncontradoException(clienteId);
        }

        Carrinho carrinho = carrinhoRepository.findByClienteIdWithItens(clienteId)
                .orElseThrow(() -> new CarrinhoNaoEncontradoException(clienteId));

        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            throw new CarrinhoVazioException(clienteId);
        }

        for (ItemCarrinho item : carrinho.getItens()) {
            Produto produto = item.getProduto();
            if (produto.getEstoque() == null || produto.getEstoque() < item.getQuantidade()) {
                throw new ProdutoSemEstoqueException(produto.getId());
            }
        }

        Pedido pedido = Pedido.builder()
                .cliente(carrinho.getCliente())
                .status(StatusPedido.CRIADO)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            Produto produto = itemCarrinho.getProduto();

            produto.setEstoque(produto.getEstoque() - itemCarrinho.getQuantidade());

            ItemPedido itemPedido = ItemPedido.builder()
                    .produto(produto)
                    .quantidade(itemCarrinho.getQuantidade())
                    .precoUnitario(itemCarrinho.getPrecoUnitario())
                    .build();

            pedido.adicionarItem(itemPedido);
            total = total.add(itemPedido.getSubtotal());
        }

        pedido.setTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);

        itemCarrinhoRepository.deleteByCarrinhoId(carrinho.getId());

        return pedidoMapper.toResponse(
                pedidoRepository.findByIdWithItens(salvo.getId())
                        .orElse(salvo)
        );
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithItens(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
        garantirDonoOuAdmin(pedido);
        return pedidoMapper.toResponse(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNaoEncontradoException(clienteId);
        }
        if (!SecurityUtils.isAdmin()
                && !SecurityUtils.getClienteIdLogado().equals(clienteId)) {
            throw new AcessoNegadoException("Você só pode listar os próprios pedidos.");
        }
        return pedidoMapper.toResponseList(
                pedidoRepository.findByClienteIdWithItens(clienteId)
        );
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithItens(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        garantirDonoOuAdmin(pedido);

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new PedidoJaCanceladoException(id);
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        return pedidoMapper.toResponse(pedido);
    }

    private void garantirDonoOuAdmin(Pedido pedido) {
        if (SecurityUtils.isAdmin()) {
            return;
        }
        if (!pedido.getCliente().getId().equals(SecurityUtils.getClienteIdLogado())) {
            throw new AcessoNegadoException("Você só pode acessar os próprios pedidos.");
        }
    }
}
