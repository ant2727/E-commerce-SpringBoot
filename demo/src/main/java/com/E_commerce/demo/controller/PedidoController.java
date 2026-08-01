package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.request.CheckoutRequest;
import com.E_commerce.demo.dto.response.PedidoResponse;
import com.E_commerce.demo.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    /**
     * Finaliza a compra: transforma o carrinho em pedido.
     * POST /pedidos/checkout
     * Body: { "clienteId": 1 }
     */
    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout(
            @Valid @RequestBody CheckoutRequest request) {

        PedidoResponse response = service.checkout(request.getClienteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    /**
     * Cancela o pedido e devolve estoque.
     * POST /pedidos/{id}/cancelar
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }
}
