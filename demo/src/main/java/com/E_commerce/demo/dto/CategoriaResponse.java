package com.E_commerce.demo.dto;

public class CategoriaResponse {

    private Long id;
    private String nome;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
