package com.upgeekapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.upgeekapi.config.JwtProperties;
import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.service.TokenService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor; // Importe a anotação
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implementação concreta do {@link TokenService} que usa a biblioteca java-jwt.
 * <p>
 * Esta é a ÚNICA classe em toda a aplicação que tem conhecimento sobre a biblioteca
 * específica de JWT, garantindo baixo acoplamento.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    /**
     * Inicializa os componentes criptográficos (algoritmo e verificador) uma única vez
     * quando o serviço é criado. A anotação {@code @PostConstruct} garante que este
     * método seja executado após a injeção de dependências, o que é mais performático.
     */
    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.issuer())
                .build();
    }

    @Override
    public String generateToken(AuthPrincipal principal) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(jwtProperties.issuer())
                .withSubject(String.valueOf(principal.userId()))
                .withClaim("roles", principal.roles())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(jwtProperties.expirationHours(), ChronoUnit.HOURS))
                .sign(algorithm);
    }

    @Override
    public Optional<AuthPrincipal> validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);

            Long userId = Long.parseLong(decodedJWT.getSubject());
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            return Optional.of(new AuthPrincipal(userId, roles));

        } catch (JWTVerificationException | NumberFormatException exception) {
            log.warn("Validação do token JWT falhou: {}", exception.getMessage());
            return Optional.empty();
        }
    }
}