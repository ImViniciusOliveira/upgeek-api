package com.upgeekapi.repository;

import com.upgeekapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório para operações de acesso a dados da entidade {@link User}.
 * Fornece métodos CRUD básicos e consultas customizadas herdadas de JpaRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca um usuário pelo seu endereço de email único.
     * O Spring Data JPA implementa este método automaticamente baseado em seu nome.
     *
     * @param email O email do usuário a ser buscado.
     * @return um {@link Optional} contendo o usuário se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByEmail(String email);
}