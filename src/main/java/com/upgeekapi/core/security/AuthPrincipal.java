package com.upgeekapi.core.security;

import java.util.List;

/**
 * Representa um principal de autenticação de forma agnóstica de framework.
 * Este é o nosso modelo interno para um usuário logado. Ele não tem nenhuma dependência
 * com o Spring Security ou com a biblioteca JWT, garantindo total desacoplamento.
 * @param userId O ID único (UUID) do nosso usuário interno.
 * @param email O email do usuário.
 * @param roles As permissões/papéis do usuário (ex: "ROLE_USER", "ROLE_ADMIN").
 */
public record AuthPrincipal(
        Long userId,
        String email,
        List<String> roles
) {}