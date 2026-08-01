package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.response.PedidoResponse;
import com.E_commerce.demo.security.SecurityUtils;
import com.E_commerce.demo.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pedidos do usuário logado.
 * Checkout não precisa mais de body com clienteId.
 */
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout() {
        PedidoResponse response = service.checkout(SecurityUtils.getClienteIdLogado());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<PedidoResponse>> listarMeus() {
        return ResponseEntity.ok(
                service.listarPorCliente(SecurityUtils.getClienteIdLogado())
        );
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }
}
