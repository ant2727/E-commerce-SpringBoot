package com.E_commerce.demo.dto;

public class ProdutoResumoResponse {

    private Long id;
    private String nome;

    public ProdutoResumoResponse() {
    }

    public ProdutoResumoResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}