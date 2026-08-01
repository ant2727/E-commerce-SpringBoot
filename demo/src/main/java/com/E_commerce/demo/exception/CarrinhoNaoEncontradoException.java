package com.E_commerce.demo.exception;

public class CarrinhoNaoEncontradoException extends RuntimeException {

    public CarrinhoNaoEncontradoException(Long clienteId) {
        super("Carrinho não encontrado para o cliente com ID " + clienteId + ".");
    }
}
