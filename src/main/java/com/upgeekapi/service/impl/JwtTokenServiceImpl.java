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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do {@link TokenService} que utiliza a biblioteca {@code com.auth0:java-jwt}.
 * <p>
 * Esta classe atua como o coração criptográfico do sistema de autenticação,
 * encapsulando toda a lógica de criação e validação de JSON Web Tokens (JWT).
 * Manter essa lógica isolada garante baixo acoplamento e facilita futuras
 * atualizações da biblioteca ou da estratégia de tokens.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements TokenService {

    // Constante para o nome da claim de papéis (roles), evitando "magic strings".
    private static final String ROLES_CLAIM = "roles";

    private final JwtProperties jwtProperties;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    /**
     * Inicializa os componentes criptográficos (algoritmo e verificador) uma única vez
     * quando o serviço é criado.
     * <p>
     * A anotação {@code @PostConstruct} garante que este método seja executado após a
     * injeção de dependências. Pré-construir esses objetos é uma otimização de
     * performance crucial, pois a criação do algoritmo e do verificador são
     * operações computacionalmente custosas.
     */
    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.issuer())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Gera um token JWT assinado com o algoritmo HMAC256. O token contém as seguintes claims:
     * <ul>
     *     <li><b>iss (Issuer):</b> O emissor do token, configurado nas propriedades.</li>
     *     <li><b>sub (Subject):</b> O ID do usuário.</li>
     *     <li><b>roles (Custom Claim):</b> Uma lista com as permissões do usuário.</li>
     *     <li><b>iat (Issued At):</b> O momento em que o token foi gerado.</li>
     *     <li><b>exp (Expiration Time):</b> O momento em que o token expirará.</li>
     * </ul>
     */
    @Override
    public String generateToken(AuthPrincipal principal) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(jwtProperties.issuer())
                .withSubject(String.valueOf(principal.userId()))
                .withClaim(ROLES_CLAIM, principal.roles())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(jwtProperties.expirationHours(), ChronoUnit.HOURS))
                .sign(algorithm);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Tenta verificar a assinatura e a validade (expiração, emissor) de um token JWT.
     * Se a verificação for bem-sucedida, extrai as informações do usuário (ID e papéis)
     * e as encapsula em um {@link AuthPrincipal}.
     * <p>
     * Falhas na validação (assinatura inválida, token expirado, etc.) são tratadas
     * de forma segura, registrando um aviso e retornando um Optional vazio,
     * o que é um comportamento esperado para tokens inválidos.
     */
    @Override
    public Optional<AuthPrincipal> validateToken(String token) {
        try {
            // 1. Verifica a assinatura, expiração e emissor do token. Lança JWTVerificationException se falhar.
            DecodedJWT decodedJWT = verifier.verify(token);

            // 2. Extrai os dados do payload do token.
            Long userId = Long.parseLong(decodedJWT.getSubject());
            List<String> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(String.class);

            // 3. Se tudo estiver correto, constrói e retorna o principal de autenticação.
            return Optional.of(new AuthPrincipal(userId, roles));

        } catch (JWTVerificationException | NumberFormatException exception) {
            // É esperado que tokens inválidos, expirados ou malformados cheguem aqui.
            // Logamos como 'warn' porque não é um erro do sistema, mas uma tentativa de acesso inválida.
            log.warn("Validação do token JWT falhou: {}", exception.getMessage());
            return Optional.empty();
        }
    }
}