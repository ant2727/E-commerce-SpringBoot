package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.request.AdicionarItemRequest;
import com.E_commerce.demo.dto.request.AtualizarQuantidadeRequest;
import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.service.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponse> adicionarProduto(
            @Valid @RequestBody AdicionarItemRequest request) {

        CarrinhoResponse response = service.adicionarProduto(
                request.getClienteId(),
                request.getProdutoId(),
                request.getQuantidade()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<CarrinhoResponse> listarCarrinho(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(service.listarCarrinho(clienteId));
    }

    @PutMapping("/{clienteId}/itens/{produtoId}")
    public ResponseEntity<Void> atualizarQuantidade(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId,
            @Valid @RequestBody AtualizarQuantidadeRequest request) {

        service.atualizarQuantidade(clienteId, produtoId, request.getQuantidade());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clienteId}/itens/{produtoId}")
    public ResponseEntity<Void> removerProduto(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId) {

        service.removerProduto(clienteId, produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clienteId}/itens")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long clienteId) {
        service.limparCarrinho(clienteId);
        return ResponseEntity.noContent().build();
    }
}
