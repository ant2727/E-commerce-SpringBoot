package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.ProdutoRequest;

import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequest request) {

        Produto produto = new Produto();

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());

        return produto;
    }

    public ProdutoResponse toResponse(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getEstoque()
        );

    }

}
