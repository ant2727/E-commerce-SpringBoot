package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.dto.response.ItemCarrinhoResponse;
import com.E_commerce.demo.entity.Carrinho;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.entity.ItemCarrinho;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.ClienteNaoEncontradoException;
import com.E_commerce.demo.exception.ItemCarrinhoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoSemEstoqueException;
import com.E_commerce.demo.repository.CarrinhoRepository;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.repository.ItemCarrinhoRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    private Carrinho buscarOuCriarCarrinho(Long clienteId) {

        return carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {

                    Cliente cliente = clienteRepository.findById(clienteId)
                            .orElseThrow(() ->
                                    new ClienteNaoEncontradoException(clienteId));

                    Carrinho carrinho = Carrinho.builder()
                            .cliente(cliente)
                            .build();

                    return carrinhoRepository.save(carrinho);

                });

    }

    public void adicionarProduto(
            Long clienteId,
            Long produtoId,
            Integer quantidade) {

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(produtoId));

        if (produto.getEstoque() < quantidade) {
            throw new ProdutoSemEstoqueException(produtoId);
        }

        Optional<ItemCarrinho> itemExistente =
                itemRepository.findByCarrinhoIdAndProdutoId(
                        carrinho.getId(),
                        produtoId
                );

        if (itemExistente.isPresent()) {

            ItemCarrinho item = itemExistente.get();

            int novaQuantidade =
                    item.getQuantidade() + quantidade;

            if (produto.getEstoque() < novaQuantidade) {
                throw new ProdutoSemEstoqueException(produtoId);
            }

            item.adicionarQuantidade(quantidade);

            itemRepository.save(item);

            return;
        }

        ItemCarrinho novoItem = ItemCarrinho.builder()
                .produto(produto)
                .carrinho(carrinho)
                .quantidade(quantidade)
                .precoUnitario(produto.getPreco())
                .build();

        itemRepository.save(novoItem);


    }

    public CarrinhoResponse listarCarrinho(Long clienteId) {

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        CarrinhoResponse response = new CarrinhoResponse();

        response.setId(carrinho.getId());
        response.setClienteId(clienteId);

        List<ItemCarrinhoResponse> itens = carrinho.getItens()
                .stream()
                .map(item -> {

                    ItemCarrinhoResponse dto = new ItemCarrinhoResponse();

                    dto.setProdutoId(item.getProduto().getId());
                    dto.setNomeProduto(item.getProduto().getNome());
                    dto.setQuantidade(item.getQuantidade());
                    dto.setPrecoUnitario(item.getPrecoUnitario());

                    dto.setSubtotal(
                            item.getPrecoUnitario()
                                    .multiply(
                                            BigDecimal.valueOf(item.getQuantidade())
                                    )
                    );

                    return dto;

                }).toList();

        response.setItens(itens);

        BigDecimal total = itens.stream()
                .map(ItemCarrinhoResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotal(total);

        return response;
    }

    @Transactional
    public void removerProduto(
            Long clienteId,
            Long produtoId) {

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        ItemCarrinho item = itemRepository
                .findByCarrinhoIdAndProdutoId(
                        carrinho.getId(),
                        produtoId
                )
                .orElseThrow(() ->
                        new ItemCarrinhoNaoEncontradoException(produtoId));

        itemRepository.delete(item);
    }
    @Transactional
    public void atualizarQuantidade(
            Long clienteId,
            Long produtoId,
            Integer quantidade) {

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        ItemCarrinho item = itemRepository
                .findByCarrinhoIdAndProdutoId(
                        carrinho.getId(),
                        produtoId
                )
                .orElseThrow(() ->
                        new ItemCarrinhoNaoEncontradoException(produtoId));

        Produto produto = item.getProduto();

        if (produto.getEstoque() < quantidade) {
            throw new ProdutoSemEstoqueException(produtoId);
        }

        item.atualizarQuantidade(quantidade);

        itemRepository.save(item);
    }
}
