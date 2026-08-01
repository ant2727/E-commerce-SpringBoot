package com.E_commerce.demo.config;

import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.enums.Role;
import com.E_commerce.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
            return;
        }

        clienteRepository.findByEmail(adminEmail).ifPresentOrElse(
                existente -> {
                    if (existente.getRole() != Role.ROLE_ADMIN) {
                        existente.setRole(Role.ROLE_ADMIN);
                        clienteRepository.save(existente);
                    }
                },
                () -> {
                    Cliente admin = new Cliente();
                    admin.setNome("Administrador");
                    admin.setEmail(adminEmail);
                    admin.setTelefone("00000000000");
                    admin.setSenha(passwordEncoder.encode(adminPassword));
                    admin.setRole(Role.ROLE_ADMIN);
                    clienteRepository.save(admin);
                }
        );
    }
}
