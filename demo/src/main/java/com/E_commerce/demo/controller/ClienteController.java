package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.ClienteRequest;
import com.E_commerce.demo.dto.ClienteResponse;
import com.E_commerce.demo.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ClienteResponse cadastrar(
            @Valid @RequestBody ClienteRequest request) {

        return service.cadastrar(request);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id);
    }

    @GetMapping
    public Page<ClienteResponse> listar(Pageable pageable) {

        return service.listar(pageable);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id) {

        service.excluir(id);
    }
}