package com.E_commerce.demo.controller;

import com.E_commerce.demo.dto.ProdutoRequest;
import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {


    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ProdutoResponse> listarTodos(Pageable pageable) {

        return service.listarTodos(pageable);

    }
    @PostMapping
    public ProdutoResponse salvar(@Valid @RequestBody ProdutoRequest request) {
        return service.salvar(request);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {

        return service.atualizar(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/buscar")
    public List<ProdutoResponse> buscarPorNome(
            @RequestParam String nome) {

        return service.buscarPorNome(nome);

    }

    @GetMapping("/buscar/preco")
    public List<ProdutoResponse> buscarPorPreco(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        return service.buscarPorPreco(min, max);

    }

    @PostMapping("/{id}/imagem")
    public ProdutoResponse uploadImagem(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo)
            throws IOException {

        return service.uploadImagem(id, arquivo);

    }
}
