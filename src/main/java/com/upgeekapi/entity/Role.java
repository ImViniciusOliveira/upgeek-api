package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.*;

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
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true, nullable = false)
    private String name;
}