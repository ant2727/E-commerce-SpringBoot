package com.E_commerce.demo.security;

import com.E_commerce.demo.exception.AcessoNegadoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Atalho para pegar o cliente logado a partir do SecurityContext.
 * Assim controllers/services não precisam receber clienteId do body/URL.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static ClienteUserDetails getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof ClienteUserDetails user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        return user;
    }

    public static Long getClienteIdLogado() {
        return getUsuarioLogado().getId();
    }

    public static boolean isAdmin() {
        return getUsuarioLogado().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
