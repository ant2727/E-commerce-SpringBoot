package com.E_commerce.demo.security;

import com.E_commerce.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * O Spring chama isso no login (e no filtro JWT) para carregar o usuário pelo e-mail.
 */
@Service
@RequiredArgsConstructor
public class ClienteUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(email)
                .map(ClienteUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}
