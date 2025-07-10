package com.upgeekapi.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum que representa os papéis de autorização no sistema (ex: ROLE_USER, ROLE_ADMIN).
 * <p>
 * Esta abordagem oferece segurança de tipos, performance e clareza,
 * sendo ideal para sistemas com um conjunto fixo de papéis.
 * Implementa {@link GrantedAuthority} para integração direta com o Spring Security.
 */
public enum RoleEnum implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        // O Spring Security usa este método para obter o nome do papel em formato de String.
        return this.name();
    }
}