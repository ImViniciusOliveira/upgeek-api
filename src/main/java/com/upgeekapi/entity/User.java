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
 * Esta entidade segue o padrão de "Rich Domain Model", onde o estado só pode ser
 * alterado através de métodos de negócio explícitos (como changePassword),
 * garantindo a consistência e a segurança dos dados.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

    public static User from(RegistrationRequestDTO request, PasswordEncoder passwordEncoder, String uniqueUsername) {
        return User.builder()
                .username(uniqueUsername)
                .email(request.email())
                .name(request.name())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(RoleEnum.ROLE_USER))
                .build();
    }

    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new BusinessRuleException("A senha atual está incorreta.");
        }
        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new BusinessRuleException("A nova senha não pode ser igual à senha atual.");
        }
        this.password = passwordEncoder.encode(newPassword);
    }

}