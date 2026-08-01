package com.E_commerce.demo.exception;

public class CarrinhoVazioException extends RuntimeException {

    public CarrinhoVazioException(Long clienteId) {
        super("Não é possível finalizar o pedido: o carrinho do cliente "
                + clienteId + " está vazio.");
    }
}
