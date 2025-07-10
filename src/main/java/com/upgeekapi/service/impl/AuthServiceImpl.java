package com.upgeekapi.service.impl;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.LoginDTO;
import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.AuthenticationException;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AuthService;
import com.upgeekapi.service.TokenService;
import com.upgeekapi.service.validation.UserUniquenessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Implementação da interface {@link AuthService}.
 * Contém a lógica de negócio para registro e autenticação de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserUniquenessValidator uniquenessValidator;

    @Override
    public LoginDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas.");
        }

        AuthPrincipal principal = AuthPrincipal.from(user);

        String token = tokenService.generateToken(principal);
        return new LoginDTO(token);
    }

    @Override
    @Transactional
    public void register(RegistrationRequestDTO request) {
        uniquenessValidator.validate(request);

        User newUser = User.builder()
                .username(generateUniqueUsername(request.email()))
                .email(request.email())
                .name(request.name())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(RoleEnum.ROLE_USER))
                .build();

        userRepository.save(newUser);
    }

    /**
     * Gera um username único a partir de um email de forma funcional e declarativa.
     * Se o username base já estiver em uso, ele gera novos nomes com sufixos
     * numéricos aleatórios de 5 dígitos até encontrar um disponível.
     */
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0]
                .replaceAll("[^a-zA-Z0-9_.-]", "")
                .toLowerCase();

        if (baseUsername.isBlank() || baseUsername.length() < 3) {
            baseUsername = "user";
        }

        final String finalBaseUsername = baseUsername;
        return Stream.iterate(finalBaseUsername, username -> finalBaseUsername + ThreadLocalRandom.current().nextInt(10000, 100000))
                .filter(username -> userRepository.findByUsername(username).isEmpty())
                .findFirst()
                .orElseThrow();
    }
}