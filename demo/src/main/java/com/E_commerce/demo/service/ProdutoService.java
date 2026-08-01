package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.ProdutoRequest;
import com.E_commerce.demo.dto.response.ProdutoResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.exception.CategoriaNaoEncontradoException;
import com.E_commerce.demo.exception.ProdutoNaoEncontradoException;
import com.E_commerce.demo.mapper.ProdutoMapper;
import com.E_commerce.demo.repository.CategoriaRepository;
import com.E_commerce.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper mapper;
    private final FileStorageService fileStorageService;

    @Transactional
    public ProdutoResponse salvar(ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new CategoriaNaoEncontradoException(request.getCategoriaId()));

        Produto produto = mapper.toEntity(request, categoria);
        return mapper.toResponse(repository.save(produto));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return mapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new CategoriaNaoEncontradoException(request.getCategoriaId()));

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());
        produto.setCategoria(categoria);

        return mapper.toResponse(repository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        repository.delete(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorPreco(BigDecimal minimo, BigDecimal maximo) {
        return repository.findByPrecoBetween(minimo, maximo)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ProdutoResponse uploadImagem(Long id, MultipartFile arquivo) throws IOException {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        String nomeArquivo = fileStorageService.salvar(arquivo);
        produto.setImagem(nomeArquivo);
        return mapper.toResponse(repository.save(produto));
    }
}
