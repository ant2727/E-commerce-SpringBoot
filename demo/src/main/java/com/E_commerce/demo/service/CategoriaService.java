package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.CategoriaRequest;
import com.E_commerce.demo.dto.response.CategoriaResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.mapper.CategoriaMapper;
import com.E_commerce.demo.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository repository, CategoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CategoriaResponse salvar(CategoriaRequest request) {

        Categoria categoria = mapper.toEntity(request);

        Categoria salva = repository.save(categoria);

        return mapper.toResponse(salva);

    }

    public List<CategoriaResponse> listarTodas() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

}
