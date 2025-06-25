package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Chave primária interna e agnóstica.

    @Column(unique = true, nullable = false)
    private String email; // O identificador único para login na nossa plataforma.

    @Column(nullable = false)
    private String name;

    @Column(name = "level")
    private int gamificationLevel = 1;

    @Column(name = "xp")
    private long experiencePoints = 0;

    // Construtor para facilitar a criação de novos usuários.
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}