package com.upgeekapi.entity;

import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.exception.custom.BusinessRuleException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa um usuário no sistema.
 * <p>
 * Esta é uma entidade JPA, mapeada para a tabela "users". Ela é focada
 * exclusivamente nos dados de identidade e perfil do usuário.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
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

    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Set<RoleEnum> roles = new HashSet<>();

    /**
     * Método de fábrica estático para criar uma nova entidade User a partir de um DTO de registro.
     * <p>
     * Encapsula a lógica de construção, incluindo a atribuição de papéis padrão e
     * a codificação da senha, mantendo o serviço de autenticação limpo.
     *
     * @param request O DTO com os dados de registro.
     * @param passwordEncoder O encoder para criptografar a senha.
     * @param uniqueUsername O nome de usuário único já gerado.
     * @return Uma nova instância de {@link User}, pronta para ser persistida.
     */
    public static User from(RegistrationRequestDTO request, PasswordEncoder passwordEncoder, String uniqueUsername) {
        return User.builder()
                .username(uniqueUsername)
                .email(request.email())
                .name(request.name())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(RoleEnum.ROLE_USER)) // Todo novo usuário começa com a role padrão.
                .build();
    }

    /**
     * Altera a senha do usuário após validar a senha atual.
     * <p>
     * Encapsula as regras de negócio para a troca de senha, como a verificação
     * da senha atual e a prevenção de que a nova senha seja igual à antiga.
     *
     * @param currentPassword A senha atual fornecida para verificação.
     * @param newPassword A nova senha a ser definida.
     * @param passwordEncoder O encoder para comparar e codificar as senhas.
     * @throws BusinessRuleException se a senha atual estiver incorreta ou se a nova senha for igual à atual.
     */
    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        // Verifica se a senha atual fornecida corresponde à senha armazenada.
        if (!passwordEncoder.matches(currentPassword, this.getPassword())) {
            throw new BusinessRuleException("A senha atual está incorreta.");
        }

        // Impede que a nova senha seja igual à antiga.
        if (passwordEncoder.matches(newPassword, this.getPassword())) {
            throw new BusinessRuleException("A nova senha não pode ser igual à senha atual.");
        }

        // Se todas as validações passarem, codifica e define a nova senha.
        this.setPassword(passwordEncoder.encode(newPassword));
    }
}