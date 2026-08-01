package com.E_commerce.demo.security;

import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void deveGerarEValidarToken() throws Exception {
        JwtService jwtService = new JwtService("segredo-de-teste", 3600000);

        Cliente cliente = new Cliente();
        cliente.setId(7L);
        cliente.setEmail("ana@email.com");
        cliente.setSenha("hash");
        cliente.setRole(Role.ROLE_CLIENTE);
        cliente.setNome("Ana");
        cliente.setTelefone("11999999999");

        ClienteUserDetails user = new ClienteUserDetails(cliente);
        String token = jwtService.gerarToken(user);

        assertNotNull(token);
        assertEquals("ana@email.com", jwtService.extrairEmail(token));
        assertTrue(jwtService.tokenValido(token, user));
    }
}
