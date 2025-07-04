package com.upgeekapi.service.impl;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.LoginDTO;
import com.upgeekapi.entity.Role;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.AuthenticationException;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.repository.RoleRepository;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AuthService;
import com.upgeekapi.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link AuthService}.
 * Contém a lógica de negócio para registro e autenticação de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public LoginDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas.");
        }

        List<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        AuthPrincipal principal = new AuthPrincipal(
                user.getId(),
                userRoles
        );

        String token = tokenService.generateToken(principal);
        return new LoginDTO(token);
    }

    @Override
    @Transactional
    public void register(RegistrationRequestDTO request) {
        validateUniqueness(request);

        String finalUsername = generateUniqueUsername(request.email());

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("A role padrão ROLE_USER não foi encontrada."));

        User newUser = User.builder()
                .username(finalUsername)
                .email(request.email())
                .name(request.name())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(defaultRole))
                .build();

        userRepository.save(newUser);
    }

    /**
     * Helper method to check if email or CPF are already in use.
     */
    private void validateUniqueness(RegistrationRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new DataConflictException("O email '" + request.email() + "' já está em uso.");
        });
        userRepository.findByCpf(request.cpf()).ifPresent(u -> {
            throw new DataConflictException("O CPF fornecido já está cadastrado.");
        });
    }

    /**
     * Generates a unique username from an email. If the base username is taken,
     * it appends a random number until a unique one is found.
     */
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0]
                .replaceAll("[^a-zA-Z0-9_.-]", "")
                .toLowerCase();

        String finalUsername = baseUsername;
        while (userRepository.findByUsername(finalUsername).isPresent()) {
            int randomNumber = ThreadLocalRandom.current().nextInt(100, 1000);
            finalUsername = baseUsername + randomNumber;
        }
        return finalUsername;
    }
}