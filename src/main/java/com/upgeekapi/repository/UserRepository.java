package com.upgeekapi.repository;

import com.upgeekapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Agora, o método principal para encontrar um usuário para login é pelo email.
    Optional<User> findByEmail(String email);
}
