package com.E_commerce.demo.exception;

public class PedidoJaCanceladoException extends RuntimeException {

    public PedidoJaCanceladoException(Long id) {
        super("O pedido com ID " + id + " já está cancelado.");
    }
}
