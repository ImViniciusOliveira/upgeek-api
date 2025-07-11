package com.upgeekapi.core.security;

import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.entity.User;

import java.util.List;

/**
 * Representa o "principal" de autenticação, ou seja, a identidade do usuário logado.
 * <p>
 * Este record é o modelo de domínio para um usuário autenticado. Ele é intencionalmente
 * agnóstico de frameworks: não possui dependências do Spring Security ou da biblioteca JWT.
 * Isso garante um design limpo e desacoplado, onde a camada de negócio interage com
 * um objeto de domínio puro, que serve como a "verdade" sobre o usuário durante uma requisição.
 *
 * @param userId O ID único (Long) do usuário, vindo da entidade {@link User}. Essencial para buscas no banco.
 * @param roles  A lista de permissões do usuário (ex: "ROLE_USER", "ROLE_ADMIN").
 *               Esta lista é usada pelo {@link com.upgeekapi.security.JwtAuthenticationFilter}
 *               para construir as autoridades que o Spring Security utiliza para controle de acesso.
 */
public record AuthPrincipal(
        Long userId,
        List<String> roles
) {
    /**
     * Factory method para criar uma instância de AuthPrincipal a partir de uma entidade {@link User}.
     * <p>
     * Encapsula a lógica de conversão da entidade de banco de dados para o objeto de
     * identidade da aplicação. Isso mantém a criação do principal centralizada e o código
     * nos serviços (como {@link com.upgeekapi.service.impl.AuthServiceImpl}) mais limpo.
     *
     * @param user A entidade {@link User} completa, da qual os dados do principal serão extraídos.
     * @return Uma nova instância imutável de {@link AuthPrincipal}.
     */
    public static AuthPrincipal from(User user) {
        // Extrai as roles da entidade e as converte para a representação em String
        // que será armazenada no token e usada pelo Spring Security.
        List<String> roleNames = user.getRoles().stream()
                .map(RoleEnum::getAuthority)
                .toList();

        return new AuthPrincipal(user.getId(), roleNames);
    }
}
