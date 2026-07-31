package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.ProdutoRequest;
import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.CategoriaNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.mapper.ProdutoMapper;
import com.E_commerce.demo.repository.CategoriaRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper mapper;
    private final FileStorageService fileStorageService;

    public ProdutoService(
            ProdutoRepository repository,
            CategoriaRepository categoriaRepository,
            ProdutoMapper mapper,
            FileStorageService fileStorageService) {

        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
        this.fileStorageService = fileStorageService;
    }

    public ProdutoResponse salvar(ProdutoRequest request) {

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new CategoriaNaoEncontradoException(request.getCategoriaId()));

        Produto produto = mapper.toEntity(request, categoria);

        Produto salvo = repository.save(produto);

        return mapper.toResponse(salvo);
    }

    public Page<ProdutoResponse> listarTodos(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);

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
                .orElseThrow(() -> new CategoriaNaoEncontradoException(request.getCategoriaId()));

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

    public List<ProdutoResponse> buscarPorNome(String nome) {

        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    public List<ProdutoResponse> buscarPorPreco(
            BigDecimal minimo,
            BigDecimal maximo) {

        return repository.findByPrecoBetween(minimo, maximo)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    public ProdutoResponse uploadImagem(
            Long id,
            MultipartFile arquivo) throws IOException {

        Produto produto = repository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(id));

        String nomeArquivo = fileStorageService.salvar(arquivo);

        produto.setImagem(nomeArquivo);

        Produto atualizado = repository.save(produto);

        return mapper.toResponse(atualizado);
    }
}