package com.upgeekapi.config;

import com.upgeekapi.entity.Role;
import com.upgeekapi.entity.User;
import com.upgeekapi.repository.RoleRepository;
import com.upgeekapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/**
 * Inicializa o banco de dados com dados essenciais e de teste.
 * Esta classe roda automaticamente na inicialização da aplicação apenas
 * quando o perfil "dev" está ativo.
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("### VERIFICANDO BANCO DE DADOS PARA POPULAÇÃO INICIAL (PERFIL DEV) ###");

            // 1. Cria as Roles (permissões) se elas não existirem
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            // 2. Cria o usuário Administrador de teste
            String adminEmail = "admin@upgeek.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                String hashedPassword = passwordEncoder.encode("admin123");
                User adminUser = new User(adminEmail, "Admin UpGeek", hashedPassword);
                adminUser.setRoles(Set.of(userRole, adminRole)); // O admin tem as duas roles
                userRepository.save(adminUser);
                System.out.println(">>> Usuário '" + adminUser.getName() + "' criado com sucesso.");
            }

            // 3. Cria o usuário Comum de teste
            String commonUserEmail = "comum@upgeek.com";
            if (userRepository.findByEmail(commonUserEmail).isEmpty()) {
                String hashedPassword = passwordEncoder.encode("senha123");
                User commonUser = new User(commonUserEmail, "Usuário Comum", hashedPassword);
                commonUser.setRoles(Set.of(userRole)); // O usuário comum tem apenas a role de usuário
                userRepository.save(commonUser);
                System.out.println(">>> Usuário '" + commonUser.getName() + "' criado com sucesso.");
            }

            System.out.println("### VERIFICAÇÃO DO BANCO DE DADOS CONCLUÍDA! ###");
        };
    }
}
