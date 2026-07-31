package com.E_commerce.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaResponse {

    private Long id;
    private String nome;
    private List<ProdutoResumoResponse> produtos = new ArrayList<>();

    public CategoriaResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<ProdutoResumoResponse> getProdutos() {
        return produtos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setProdutos(List<ProdutoResumoResponse> produtos) {
        this.produtos = produtos;
    }
}