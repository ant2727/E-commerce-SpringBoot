package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.ProdutoRequest;
import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.mapper.ProdutoMapper;
import com.E_commerce.demo.repository.CategoriaRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper mapper;

    public ProdutoService(
            ProdutoRepository repository,
            CategoriaRepository categoriaRepository,
            ProdutoMapper mapper) {

        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    public ProdutoResponse salvar(ProdutoRequest request) {

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = mapper.toEntity(request, categoria);

        Produto salvo = repository.save(produto);

        return mapper.toResponse(salvo);
    }

    public List<ProdutoResponse> listarTodos() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        return mapper.toResponse(produto);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());
        produto.setCategoria(categoria);

        Produto atualizado = repository.save(produto);

        return mapper.toResponse(atualizado);
    }

    public void excluir(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        repository.delete(produto);
    }
}