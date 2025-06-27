package com.upgeekapi.service.impl;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.response.LoginResponseDTO;
import com.upgeekapi.entity.Role;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.AuthenticationException;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AuthService;
import com.upgeekapi.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas.");
        }

        // A MUDANÇA: Pega as roles reais do banco de dados e as converte em strings
        List<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        AuthPrincipal principal = new AuthPrincipal(
                user.getId(),
                user.getEmail(),
                userRoles // Passa as roles reais para o token
        );

        String token = tokenService.generateToken(principal);
        return new LoginResponseDTO(token);
    }
}