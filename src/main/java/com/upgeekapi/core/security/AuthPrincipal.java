package com.upgeekapi.core.security;

import java.util.List;

/**
 * Representa um principal de autenticação de forma agnóstica de framework.
 * Este é o modelo interno para um usuário logado. Ele não tem nenhuma dependência
 * com o Spring Security ou com a biblioteca JWT, garantindo total desacoplamento e
 * permitindo que a camada de negócio interaja com um objeto de domínio limpo.
 *
 * @param userId O ID único (Long) do nosso usuário interno, vindo da entidade User.
 * @param roles As permissões/papéis do usuário (ex: "ROLE_USER", "ROLE_ADMIN"),
 * usadas pelo Spring Security para controle de acesso.
 */
public record AuthPrincipal(
        Long userId,
        List<String> roles
) {}
