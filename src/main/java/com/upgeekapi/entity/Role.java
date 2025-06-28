package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma permissão ou papel de autorização no sistema (ex: ROLE_USER, ROLE_ADMIN).
 * Esta é uma entidade JPA, mapeada para a tabela "roles" no banco de dados,
 * e é usada pelo Spring Security para controle de acesso baseado em papéis.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true, nullable = false)
    private String name;

    /**
     * Construtor para facilitar a criação de novas instâncias de Role.
     * @param name O nome da role (ex: "ROLE_USER").
     */
    public Role(String name) {
        this.name = name;
    }
}
