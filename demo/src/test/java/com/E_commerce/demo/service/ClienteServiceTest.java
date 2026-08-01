package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.ClienteRequest;
import com.E_commerce.demo.dto.response.ClienteResponse;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.mapper.ClienteMapper;
import com.E_commerce.demo.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;
    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteService service;

    @Test
    void deveCadastrarCliente() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("Ana");
        request.setEmail("ana@email.com");
        request.setTelefone("11999999999");

        Cliente entidade = new Cliente();
        entidade.setNome("Ana");
        entidade.setEmail("ana@email.com");
        entidade.setTelefone("11999999999");

        Cliente salvo = new Cliente();
        salvo.setId(1L);
        salvo.setNome("Ana");
        salvo.setEmail("ana@email.com");
        salvo.setTelefone("11999999999");

        ClienteResponse response = new ClienteResponse();
        response.setId(1L);
        response.setNome("Ana");
        response.setEmail("ana@email.com");
        response.setTelefone("11999999999");

        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        when(mapper.toEntity(request)).thenReturn(entidade);
        when(repository.save(entidade)).thenReturn(salvo);
        when(mapper.toResponse(salvo)).thenReturn(response);

        ClienteResponse resultado = service.cadastrar(request);

        assertEquals(1L, resultado.getId());
        assertEquals("ana@email.com", resultado.getEmail());
    }

    @Test
    void deveRecusarEmailDuplicado() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("Ana");
        request.setEmail("ana@email.com");
        request.setTelefone("11999999999");

        when(repository.findByEmail("ana@email.com"))
                .thenReturn(Optional.of(new Cliente()));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(request));
        verify(repository, never()).save(any());
    }
}
