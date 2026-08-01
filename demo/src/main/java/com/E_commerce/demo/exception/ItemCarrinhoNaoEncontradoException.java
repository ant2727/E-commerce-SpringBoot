package com.E_commerce.demo.exception;

public class ItemCarrinhoNaoEncontradoException extends RuntimeException {

    public ItemCarrinhoNaoEncontradoException(Long produtoId) {
        super("O produto com ID " + produtoId + " não está no carrinho.");
    }
}
