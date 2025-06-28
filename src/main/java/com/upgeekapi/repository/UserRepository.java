package com.upgeekapi.repository;

import com.upgeekapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade {@link User}.
 * Fornece métodos CRUD básicos e consultas customizadas herdadas de JpaRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu endereço de email único.
     * O Spring Data JPA implementa este método automaticamente baseado em seu nome.
     *
     * @param email O email do usuário a ser buscado.
     * @return um {@link Optional} contendo o usuário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca um usuário pelo seu nome de usuário único.
     * Utilizado para garantir a unicidade do nome de usuário durante o registro.
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
