package com.E_commerce.demo.exception;

public class CategoriaNaoEncontradoException extends RuntimeException {

    public CategoriaNaoEncontradoException(Long id) {
        super("Categoria com id " + id + " não encontrada.");
    }

}