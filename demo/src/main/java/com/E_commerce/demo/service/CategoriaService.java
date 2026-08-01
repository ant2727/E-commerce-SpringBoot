package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.CategoriaRequest;
import com.E_commerce.demo.dto.response.CategoriaResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.exception.CategoriaNaoEncontradoException;
import com.E_commerce.demo.mapper.CategoriaMapper;
import com.E_commerce.demo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    @Transactional
    public CategoriaResponse salvar(CategoriaRequest request) {
        Categoria categoria = mapper.toEntity(request);
        Categoria salva = repository.save(categoria);
        return mapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = repository.findByIdWithProdutos(id)
                .orElseThrow(() -> new CategoriaNaoEncontradoException(id));
        return mapper.toResponseComProdutos(categoria);
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradoException(id));

        categoria.setNome(request.getNome());
        return mapper.toResponse(repository.save(categoria));
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradoException(id));
        repository.delete(categoria);
    }
}
