package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.ProdutoRequest;

import com.E_commerce.demo.dto.ProdutoResponse;
import com.E_commerce.demo.entity.Categoria;
import com.E_commerce.demo.entity.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequest request, Categoria categoria) {

        Produto produto = new Produto();

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());
        produto.setCategoria(categoria);

        return produto;
    }

    public ProdutoResponse toResponse(Produto produto) {

        ProdutoResponse response = new ProdutoResponse();

        response.setId(produto.getId());
        response.setNome(produto.getNome());
        response.setPreco(produto.getPreco());
        response.setEstoque(produto.getEstoque());
        response.setImagem(produto.getImagem());

        return response;
    }
}