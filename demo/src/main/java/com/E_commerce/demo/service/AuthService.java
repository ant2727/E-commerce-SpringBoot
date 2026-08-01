package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.ClienteRequest;
import com.E_commerce.demo.dto.request.LoginRequest;
import com.E_commerce.demo.dto.response.AuthResponse;
import com.E_commerce.demo.dto.response.ClienteResponse;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.enums.Role;
import com.E_commerce.demo.exception.CredenciaisInvalidasException;
import com.E_commerce.demo.mapper.ClienteMapper;
import com.E_commerce.demo.repository.ClienteRepository;
import com.E_commerce.demo.security.ClienteUserDetails;
import com.E_commerce.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cadastro + login.
 *
 * register: salva senha com BCrypt e devolve JWT
 * login: AuthenticationManager confere email/senha e devolve JWT
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse registrar(ClienteRequest request) {
        clienteRepository.findByEmail(request.getEmail())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("E-mail já cadastrado.");
                });

        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setSenha(passwordEncoder.encode(request.getSenha()));
        cliente.setRole(Role.ROLE_CLIENTE);

        Cliente salvo = clienteRepository.save(cliente);
        return gerarAuthResponse(salvo);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException();
        }

        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(CredenciaisInvalidasException::new);

        return gerarAuthResponse(cliente);
    }

    private AuthResponse gerarAuthResponse(Cliente cliente) {
        ClienteUserDetails userDetails = new ClienteUserDetails(cliente);
        String token = jwtService.gerarToken(userDetails);

        return new AuthResponse(
                token,
                cliente.getId(),
                cliente.getEmail(),
                cliente.getRole().name()
        );
    }

    public ClienteResponse toClienteResponse(Cliente cliente) {
        return clienteMapper.toResponse(cliente);
    }
}
