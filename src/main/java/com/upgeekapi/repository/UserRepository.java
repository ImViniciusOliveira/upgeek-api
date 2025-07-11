package com.upgeekapi.repository;

import com.upgeekapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade {@link User}.
 * <p>
 * Estende {@link JpaRepository} para obter métodos CRUD prontos e define
 * consultas customizadas que o Spring Data JPA implementa automaticamente com base
 * na convenção de nomenclatura de métodos.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu endereço de email único. Essencial para o processo de login.
     *
     * @param email O email do usuário a ser buscado.
     * @return um {@link Optional} contendo o usuário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca um usuário pelo seu nome de usuário único.
     * Utilizado para garantir a unicidade do nome de usuário durante o registro e atualização.
     *
     * @param username O nome de usuário a ser buscado.
     * @return um {@link Optional} contendo o usuário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca um usuário pelo seu CPF único.
     * Utilizado para garantir a unicidade do CPF durante o registro.
     *
     * @param cpf O CPF do usuário a ser buscado.
     * @return um {@link Optional} contendo o usuário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByCpf(String cpf);
}