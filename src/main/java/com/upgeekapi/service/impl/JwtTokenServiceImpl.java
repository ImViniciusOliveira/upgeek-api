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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implementação concreta do TokenService que usa a biblioteca java-jwt.
 * Esta é a ÚNICA classe em toda a aplicação que tem conhecimento sobre a biblioteca
 * específica de JWT, garantindo baixo acoplamento.
 */
@Service
public class JwtTokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenServiceImpl.class);
    private final JwtProperties jwtProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtTokenServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * O método @PostConstruct garante que o algoritmo e o verificador sejam
     * inicializados uma única vez quando o serviço é criado, o que é mais performático.
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
                .withClaim("email", principal.email())
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
            String email = decodedJWT.getClaim("email").asString();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            AuthPrincipal principal = new AuthPrincipal(userId, email, roles);
            return Optional.of(principal);

        } catch (JWTVerificationException exception) {
            logger.warn("Validação do token falhou: {}", exception.getMessage());
            return Optional.empty();
        }
    }
}