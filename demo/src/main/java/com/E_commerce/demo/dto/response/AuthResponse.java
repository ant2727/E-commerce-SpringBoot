package com.E_commerce.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tipo = "Bearer";
    private Long clienteId;
    private String email;
    private String role;

    public AuthResponse(String token, Long clienteId, String email, String role) {
        this.token = token;
        this.clienteId = clienteId;
        this.email = email;
        this.role = role;
    }
}
