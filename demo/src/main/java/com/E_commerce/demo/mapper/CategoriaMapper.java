package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.CategoriaRequest;
import com.E_commerce.demo.dto.CategoriaResponse;
import com.E_commerce.demo.dto.ProdutoResumoResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.entity.Produto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequest request) {

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());

        return categoria;
    }

    public CategoriaResponse toResponse(Categoria categoria) {

        CategoriaResponse response = new CategoriaResponse();

        response.setId(categoria.getId());
        response.setNome(categoria.getNome());

        List<ProdutoResumoResponse> produtos = categoria.getProdutos()
                .stream()
                .map(this::toProdutoResumo)
                .toList();

        response.setProdutos(produtos);

        return response;
    }

    private ProdutoResumoResponse toProdutoResumo(Produto produto) {

        ProdutoResumoResponse response = new ProdutoResumoResponse();

        response.setId(produto.getId());
        response.setNome(produto.getNome());

        return response;
    }
}

