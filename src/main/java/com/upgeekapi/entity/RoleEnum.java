package com.upgeekapi.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum que representa os papéis (roles) de autorização no sistema.
 * <p>
 * Esta abordagem oferece segurança de tipos, performance e clareza, sendo ideal
 * para sistemas com um conjunto fixo de papéis. Ao implementar {@link GrantedAuthority},
 * este enum se integra diretamente ao ecossistema do Spring Security, permitindo seu
 * uso em anotações como {@code @PreAuthorize("hasRole('ADMIN')")}.
 */
public enum RoleEnum implements GrantedAuthority {

    /**
     * Papel padrão para usuários autenticados. Concede acesso a funcionalidades
     * gerais da plataforma, como visualizar produtos e gerenciar a própria conta.
     */
    ROLE_USER,

    /**
     * Papel de administrador. Concede acesso irrestrito a todas as funcionalidades,
     * incluindo o gerenciamento de produtos, usuários e configurações do sistema.
     */
    ROLE_ADMIN;

    /**
     * Retorna a representação em String da autoridade (o nome do papel).
     * <p>
     * Este método é o ponto central da integração com o Spring Security.
     * O framework invoca este método para obter o nome da role que será usado
     * em suas verificações de autorização.
     *
     * @return O nome do enum (ex: "ROLE_USER"), que serve como o identificador da autoridade.
     */
    @Override
    public String getAuthority() {
        // A implementação padrão e correta é retornar o nome da própria constante do enum.
        // O Spring Security, por convenção, espera que os papéis comecem com o prefixo "ROLE_".
        return this.name();
    }
}