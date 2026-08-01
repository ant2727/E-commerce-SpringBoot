package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.entity.Carrinho;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.entity.ItemCarrinho;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.CarrinhoNaoEncontradoException;
import com.E_commerce.demo.exception.ClienteNaoEncontradoException;
import com.E_commerce.demo.exception.ItemCarrinhoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoSemEstoqueException;
import com.E_commerce.demo.mapper.CarrinhoMapper;
import com.E_commerce.demo.repository.CarrinhoRepository;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.repository.ItemCarrinhoRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoMapper carrinhoMapper;

    private Carrinho buscarOuCriarCarrinho(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Cliente cliente = clienteRepository.findById(clienteId)
                            .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));

                    Carrinho carrinho = Carrinho.builder()
                            .cliente(cliente)
                            .build();

                    return carrinhoRepository.save(carrinho);
                });
    }

    private Carrinho buscarCarrinhoExistente(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new CarrinhoNaoEncontradoException(clienteId));
    }

    private void validarEstoque(Produto produto, int quantidade) {
        if (produto.getEstoque() == null || produto.getEstoque() < quantidade) {
            throw new ProdutoSemEstoqueException(produto.getId());
        }
    }

    @Transactional
    public CarrinhoResponse adicionarProduto(
            Long clienteId,
            Long produtoId,
            Integer quantidade) {

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        validarEstoque(produto, quantidade);

        itemRepository.findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .ifPresentOrElse(
                        item -> {
                            int novaQuantidade = item.getQuantidade() + quantidade;
                            validarEstoque(produto, novaQuantidade);
                            item.adicionarQuantidade(quantidade);
                            itemRepository.save(item);
                        },
                        () -> {
                            ItemCarrinho novoItem = ItemCarrinho.builder()
                                    .produto(produto)
                                    .carrinho(carrinho)
                                    .quantidade(quantidade)
                                    .precoUnitario(produto.getPreco())
                                    .build();
                            itemRepository.save(novoItem);
                        }
                );

        return carrinhoRepository.findByClienteIdWithItens(clienteId)
                .map(carrinhoMapper::toResponse)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }

    @Transactional(readOnly = true)
    public CarrinhoResponse listarCarrinho(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNaoEncontradoException(clienteId);
        }

        return carrinhoRepository.findByClienteIdWithItens(clienteId)
                .map(carrinhoMapper::toResponse)
                .orElseGet(() -> carrinhoVazio(clienteId));
    }

    private CarrinhoResponse carrinhoVazio(Long clienteId) {
        CarrinhoResponse response = new CarrinhoResponse();
        response.setClienteId(clienteId);
        response.setItens(Collections.emptyList());
        response.setTotal(BigDecimal.ZERO);
        return response;
    }

    @Transactional
    public void removerProduto(Long clienteId, Long produtoId) {
        Carrinho carrinho = buscarCarrinhoExistente(clienteId);

        ItemCarrinho item = itemRepository
                .findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .orElseThrow(() -> new ItemCarrinhoNaoEncontradoException(produtoId));

        itemRepository.delete(item);
    }

    @Transactional
    public void atualizarQuantidade(
            Long clienteId,
            Long produtoId,
            Integer quantidade) {

        Carrinho carrinho = buscarCarrinhoExistente(clienteId);

        ItemCarrinho item = itemRepository
                .findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .orElseThrow(() -> new ItemCarrinhoNaoEncontradoException(produtoId));

        validarEstoque(item.getProduto(), quantidade);
        item.atualizarQuantidade(quantidade);
        itemRepository.save(item);
    }

    @Transactional
    public void limparCarrinho(Long clienteId) {
        carrinhoRepository.findByClienteId(clienteId)
                .ifPresent(carrinho -> itemRepository.deleteByCarrinhoId(carrinho.getId()));
    }
}
