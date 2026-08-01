package com.E_commerce.demo.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(Long id) {

        super("Cliente com ID " + id + " não encontrado.");

    }
}
