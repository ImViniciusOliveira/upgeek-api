package com.upgeekapi.repository;

import com.upgeekapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade {@link Role}.
 * Fornece métodos CRUD básicos e consultas customizadas herdadas de JpaRepository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca uma role pelo seu nome único.
     * O Spring Data JPA implementa este método automaticamente baseado em seu nome.
     *
     * @param name O nome da role a ser buscada (ex: "ROLE_USER").
     * @return um {@link Optional} contendo a role se encontrada, ou um Optional vazio caso contrário.
     */
    Optional<Role> findByName(String name);
}
