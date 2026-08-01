package com.E_commerce.demo.controller;


import com.E_commerce.demo.dto.request.AdicionarItemRequest;
import com.E_commerce.demo.dto.response.CarrinhoResponse;
import com.E_commerce.demo.service.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping("/adicionar")
    public ResponseEntity<Void> adicionarProduto(
            @Valid @RequestBody AdicionarItemRequest request) {

        service.adicionarProduto(
                request.getClienteId(),
                request.getProdutoId(),
                request.getQuantidade()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<CarrinhoResponse> listarCarrinho(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                service.listarCarrinho(clienteId)
        );
    }
}
