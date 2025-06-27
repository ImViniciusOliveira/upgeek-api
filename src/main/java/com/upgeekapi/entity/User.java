package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Representa um usuário no sistema.
 * Agora inclui um campo para a senha criptografada.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password; // NOVO: Campo para armazenar a senha HASHED

    @Column(name = "level")
    private int gamificationLevel = 1;

    @Column(name = "xp")
    private long experiencePoints = 0;

    @ManyToMany(fetch = FetchType.EAGER) // EAGER para que as roles sejam carregadas junto com o usuário
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}