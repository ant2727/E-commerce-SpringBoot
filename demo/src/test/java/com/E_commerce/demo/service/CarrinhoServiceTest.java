package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.entity.Carrinho;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.entity.ItemCarrinho;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.ClienteNaoEncontradoException;
import com.E_commerce.demo.exception.ItemCarrinhoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoSemEstoqueException;
import com.E_commerce.demo.mapper.CarrinhoMapper;
import com.E_commerce.demo.repository.CarrinhoRepository;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.repository.ItemCarrinhoRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ItemCarrinhoRepository itemRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private CarrinhoMapper carrinhoMapper;

    @InjectMocks
    private CarrinhoService carrinhoService;

    private Cliente cliente;
    private Produto produto;
    private Carrinho carrinho;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Ana");
        cliente.setEmail("ana@email.com");
        cliente.setTelefone("11999999999");

        produto = new Produto();
        produto.setId(10L);
        produto.setNome("Notebook");
        produto.setPreco(new BigDecimal("3500.00"));
        produto.setEstoque(5);

        carrinho = Carrinho.builder()
                .id(100L)
                .cliente(cliente)
                .itens(new ArrayList<>())
                .build();
    }

    @Test
    void deveAdicionarProdutoNovoAoCarrinho() {
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(itemRepository.findByCarrinhoIdAndProdutoId(100L, 10L)).thenReturn(Optional.empty());
        when(itemRepository.save(any(ItemCarrinho.class))).thenAnswer(inv -> inv.getArgument(0));

        CarrinhoResponse esperado = new CarrinhoResponse();
        esperado.setId(100L);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoMapper.toResponse(carrinho)).thenReturn(esperado);

        CarrinhoResponse response = carrinhoService.adicionarProduto(1L, 10L, 2);

        assertEquals(100L, response.getId());
        verify(itemRepository).save(any(ItemCarrinho.class));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        assertThrows(
                ProdutoSemEstoqueException.class,
                () -> carrinhoService.adicionarProduto(1L, 10L, 10)
        );

        verify(itemRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> carrinhoService.adicionarProduto(1L, 99L, 1)
        );
    }

    @Test
    void deveRetornarCarrinhoVazioQuandoNaoExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.empty());

        CarrinhoResponse response = carrinhoService.listarCarrinho(1L);

        assertEquals(1L, response.getClienteId());
        assertTrue(response.getItens().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getTotal());
    }

    @Test
    void deveLancarExcecaoAoListarCarrinhoDeClienteInexistente() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                ClienteNaoEncontradoException.class,
                () -> carrinhoService.listarCarrinho(99L)
        );
    }

    @Test
    void deveRemoverProdutoDoCarrinho() {
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));

        ItemCarrinho item = ItemCarrinho.builder()
                .id(1L)
                .produto(produto)
                .carrinho(carrinho)
                .quantidade(1)
                .precoUnitario(produto.getPreco())
                .build();

        when(itemRepository.findByCarrinhoIdAndProdutoId(100L, 10L))
                .thenReturn(Optional.of(item));

        carrinhoService.removerProduto(1L, 10L);

        verify(itemRepository).delete(item);
    }

    @Test
    void deveLancarExcecaoAoRemoverItemInexistente() {
        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(itemRepository.findByCarrinhoIdAndProdutoId(100L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ItemCarrinhoNaoEncontradoException.class,
                () -> carrinhoService.removerProduto(1L, 10L)
        );
    }

    @Test
    void deveSomarQuantidadeQuandoProdutoJaExiste() {
        ItemCarrinho item = ItemCarrinho.builder()
                .id(1L)
                .produto(produto)
                .carrinho(carrinho)
                .quantidade(2)
                .precoUnitario(produto.getPreco())
                .build();

        when(carrinhoRepository.findByClienteId(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(itemRepository.findByCarrinhoIdAndProdutoId(100L, 10L))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoMapper.toResponse(carrinho)).thenReturn(new CarrinhoResponse());

        carrinhoService.adicionarProduto(1L, 10L, 1);

        assertEquals(3, item.getQuantidade());
        verify(itemRepository).save(item);
    }
}
