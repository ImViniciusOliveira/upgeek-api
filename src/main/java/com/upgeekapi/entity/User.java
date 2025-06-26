package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * Representa um usuário no sistema.
 * Esta é uma entidade JPA, mapeada para a tabela "users" no banco de dados.
 * A identidade do usuário é gerenciada internamente por um UUID, garantindo
 * desacoplamento de provedores de autenticação externos.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // TODO: O email como identificador principal é uma solução temporária.
    // Quando a autenticação for implementada, o identificador virá do token (ex: 'sub')
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "level")
    private int gamificationLevel = 1;

    @Column(name = "xp")
    private long experiencePoints = 0;

    /**
     * Construtor para facilitar a criação de novas instâncias de User com
     * os dados essenciais para o registro inicial.
     * @param email O email único do usuário, que serve como seu identificador de login TEMPORARIO.
     * @param name O nome de exibição público do usuário.
     */
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}