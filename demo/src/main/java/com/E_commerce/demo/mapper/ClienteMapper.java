package com.E_commerce.demo.mapper;

import com.E_commerce.demo.dto.ClienteRequest;
import com.E_commerce.demo.dto.ClienteResponse;
import com.E_commerce.demo.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest request) {

        Cliente cliente = new Cliente();

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());

        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente) {

        ClienteResponse response = new ClienteResponse();

        response.setId(cliente.getId());
        response.setNome(cliente.getNome());
        response.setEmail(cliente.getEmail());
        response.setTelefone(cliente.getTelefone());

        return response;
    }
}
