package com.E_commerce.demo.exception;

public class ProdutoSemEstoqueException extends RuntimeException {

    public ProdutoSemEstoqueException(Long produtoId) {
        super("O produto com ID " + produtoId + " não possui estoque suficiente.");
    }

}
