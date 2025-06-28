package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String password;

    @Column(name = "level")
    private int gamificationLevel = 1;

    @Column(name = "xp")
    private long experiencePoints = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String name, String cpf, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
    }

    /**
     * Compara este User com outro objeto para verificar a igualdade.
     * A comparação é baseada no 'id' da entidade, que é a prática recomendada
     * para entidades JPA após serem persistidas.
     *
     * @param o O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // O ID é usado para a verificação de igualdade, pois é a chave primária única.
        // A verificação 'id != null' garante o comportamento correto para entidades ainda não persistidas.
        return id != null && id.equals(user.id);
    }

    /**
     * Retorna um valor de hash para o objeto.
     * Usar um valor fixo ou o hash da classe é uma estratégia segura para entidades JPA
     * para evitar que o hash mude quando o ID é gerado, o que pode quebrar coleções como HashSets.
     *
     * @return O valor de hash para o objeto.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}