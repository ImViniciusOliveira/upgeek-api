package com.upgeekapi.core.security;

import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.entity.User;

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
) {
    /**
     * Factory method para criar uma instância de AuthPrincipal a partir de uma entidade User.
     * Encapsula a lógica de extrair as roles, tornando o código na camada de serviço mais limpo.
     *
     * @param user A entidade User da qual o principal será criado.
     * @return Uma nova instância de AuthPrincipal.
     */
    public static AuthPrincipal from(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(RoleEnum::getAuthority)
                .toList();

        return new AuthPrincipal(user.getId(), roleNames);
    }
}