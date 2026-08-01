package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.request.AdicionarItemRequest;
import com.E_commerce.demo.dto.request.AtualizarQuantidadeRequest;
import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.security.SecurityUtils;
import com.E_commerce.demo.service.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Carrinho do usuário logado.
 * Não recebe mais clienteId: vem do JWT via SecurityUtils.
 */
@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponse> adicionarProduto(
            @Valid @RequestBody AdicionarItemRequest request) {

        Long clienteId = SecurityUtils.getClienteIdLogado();

        CarrinhoResponse response = service.adicionarProduto(
                clienteId,
                request.getProdutoId(),
                request.getQuantidade()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CarrinhoResponse> listarCarrinho() {
        return ResponseEntity.ok(
                service.listarCarrinho(SecurityUtils.getClienteIdLogado())
        );
    }

    @PutMapping("/itens/{produtoId}")
    public ResponseEntity<Void> atualizarQuantidade(
            @PathVariable Long produtoId,
            @Valid @RequestBody AtualizarQuantidadeRequest request) {

        service.atualizarQuantidade(
                SecurityUtils.getClienteIdLogado(),
                produtoId,
                request.getQuantidade()
        );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/itens/{produtoId}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long produtoId) {
        service.removerProduto(SecurityUtils.getClienteIdLogado(), produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/itens")
    public ResponseEntity<Void> limparCarrinho() {
        service.limparCarrinho(SecurityUtils.getClienteIdLogado());
        return ResponseEntity.noContent().build();
    }
}
