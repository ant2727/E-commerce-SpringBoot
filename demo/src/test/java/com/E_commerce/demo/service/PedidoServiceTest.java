package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.response.PedidoResponse;
import com.E_commerce.demo.entity.*;
import com.E_commerce.demo.enums.Role;
import com.E_commerce.demo.enums.StatusPedido;
import com.E_commerce.demo.exception.CarrinhoVazioException;
import com.E_commerce.demo.exception.PedidoJaCanceladoException;
import com.E_commerce.demo.exception.PedidoNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoSemEstoqueException;
import com.E_commerce.demo.mapper.PedidoMapper;
import com.E_commerce.demo.repository.CarrinhoRepository;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.repository.ItemCarrinhoRepository;
import com.E_commerce.demo.repository.PedidoRepository;
import com.E_commerce.demo.security.ClienteUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ItemCarrinhoRepository itemCarrinhoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;

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
        cliente.setSenha("hash");
        cliente.setRole(Role.ROLE_CLIENTE);

        produto = new Produto();
        produto.setId(10L);
        produto.setNome("Notebook");
        produto.setPreco(new BigDecimal("3500.00"));
        produto.setEstoque(5);

        ItemCarrinho item = ItemCarrinho.builder()
                .id(1L)
                .produto(produto)
                .quantidade(2)
                .precoUnitario(produto.getPreco())
                .build();

        carrinho = Carrinho.builder()
                .id(100L)
                .cliente(cliente)
                .itens(new ArrayList<>(List.of(item)))
                .build();
        item.setCarrinho(carrinho);

        ClienteUserDetails userDetails = new ClienteUserDetails(cliente);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveFazerCheckoutComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.of(carrinho));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(50L);
            return p;
        });
        when(pedidoRepository.findByIdWithItens(50L)).thenAnswer(inv -> {
            Pedido p = Pedido.builder()
                    .id(50L)
                    .cliente(cliente)
                    .status(StatusPedido.CRIADO)
                    .total(new BigDecimal("7000.00"))
                    .itens(List.of())
                    .build();
            return Optional.of(p);
        });

        PedidoResponse esperado = new PedidoResponse();
        esperado.setId(50L);
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(esperado);

        PedidoResponse response = pedidoService.checkout(1L);

        assertEquals(50L, response.getId());
        assertEquals(3, produto.getEstoque());
        verify(itemCarrinhoRepository).deleteByCarrinhoId(100L);

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        assertEquals(new BigDecimal("7000.00"), captor.getValue().getTotal());
        assertEquals(StatusPedido.CRIADO, captor.getValue().getStatus());
    }

    @Test
    void deveRecusarCheckoutComCarrinhoVazio() {
        carrinho.setItens(new ArrayList<>());
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.of(carrinho));

        assertThrows(CarrinhoVazioException.class, () -> pedidoService.checkout(1L));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveRecusarCheckoutSemEstoque() {
        produto.setEstoque(1);
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(carrinhoRepository.findByClienteIdWithItens(1L)).thenReturn(Optional.of(carrinho));

        assertThrows(ProdutoSemEstoqueException.class, () -> pedidoService.checkout(1L));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveCancelarPedidoEDevolverEstoque() {
        ItemPedido itemPedido = ItemPedido.builder()
                .produto(produto)
                .quantidade(2)
                .precoUnitario(produto.getPreco())
                .build();

        Pedido pedido = Pedido.builder()
                .id(50L)
                .cliente(cliente)
                .status(StatusPedido.CRIADO)
                .total(new BigDecimal("7000.00"))
                .itens(new ArrayList<>(List.of(itemPedido)))
                .build();
        itemPedido.setPedido(pedido);

        when(pedidoRepository.findByIdWithItens(50L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(new PedidoResponse());

        produto.setEstoque(3);

        pedidoService.cancelar(50L);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        assertEquals(5, produto.getEstoque());
    }

    @Test
    void deveRecusarCancelarPedidoJaCancelado() {
        Pedido pedido = Pedido.builder()
                .id(50L)
                .cliente(cliente)
                .status(StatusPedido.CANCELADO)
                .total(BigDecimal.ZERO)
                .itens(new ArrayList<>())
                .build();

        when(pedidoRepository.findByIdWithItens(50L)).thenReturn(Optional.of(pedido));

        assertThrows(PedidoJaCanceladoException.class, () -> pedidoService.cancelar(50L));
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoExiste() {
        when(pedidoRepository.findByIdWithItens(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPorId(99L));
    }
}
