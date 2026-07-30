package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.CategoriaRequest;
import com.E_commerce.demo.dto.CategoriaResponse;
import com.E_commerce.demo.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequest request) {

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());

        return categoria;
    }

    public CategoriaResponse toResponse(Categoria categoria) {

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );

    }

}