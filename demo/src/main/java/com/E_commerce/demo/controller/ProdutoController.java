package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.ProdutoRequest;
import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {


    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutoResponse> listarTodos() {
        return service.listarTodos();
    }

    @PostMapping
    public ProdutoResponse salvar(@Valid @RequestBody ProdutoRequest request) {
        return service.salvar(request);
    }
}
