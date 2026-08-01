package com.E_commerce.demo.security;

import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adaptador: transforma nosso Cliente no formato que o Spring Security entende (UserDetails).
 *
 * username  -> email
 * password  -> senha criptografada
 * authorities -> role (ROLE_CLIENTE / ROLE_ADMIN)
 */
@Getter
public class ClienteUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String senha;
    private final Role role;

    public ClienteUserDetails(Cliente cliente) {
        this.id = cliente.getId();
        this.email = cliente.getEmail();
        this.senha = cliente.getSenha();
        this.role = cliente.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
