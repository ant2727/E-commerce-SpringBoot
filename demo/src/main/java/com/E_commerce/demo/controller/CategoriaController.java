package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.CategoriaRequest;
import com.E_commerce.demo.dto.CategoriaResponse;
import com.E_commerce.demo.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    public CategoriaResponse salvar(@Valid @RequestBody CategoriaRequest request) {
        return service.salvar(request);
    }

    @GetMapping
    public List<CategoriaResponse> listarTodas() {
        return service.listarTodas();
    }

}
