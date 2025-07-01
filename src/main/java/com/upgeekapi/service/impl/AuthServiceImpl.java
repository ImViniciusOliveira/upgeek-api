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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link AuthService}.
 * Contém a lógica de negócio para registro e autenticação de usuários.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

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
                user.getEmail(),
                userRoles
        );

        String token = tokenService.generateToken(principal);
        return new LoginDTO(token);
    }

    @Override
    @Transactional
    public void register(RegistrationRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DataConflictException("O email '" + request.email() + "' já está em uso.");
        }
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DataConflictException("O nome de usuário '" + request.username() + "' já está em uso.");
        }
        if (userRepository.findByCpf(request.cpf()).isPresent()) {
            throw new DataConflictException("O CPF fornecido já está cadastrado.");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("A role padrão ROLE_USER não foi encontrada no banco de dados."));

        String hashedPassword = passwordEncoder.encode(request.password());

        User newUser = new User(
                request.username(),
                request.email(),
                request.name(),
                request.cpf(),
                hashedPassword
        );
        newUser.setRoles(Set.of(defaultRole));

        userRepository.save(newUser);
    }
}
