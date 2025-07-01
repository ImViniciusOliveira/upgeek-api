package com.upgeekapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration; //ainda n consertei

/**
 * Mapeia as configurações de JWT do arquivo 'application.properties' para um objeto Java imutável.
 * <p>
 * A anotação {@code @Configuration} a torna um bean gerenciado pelo Spring,
 * permitindo que {@code @ConfigurationProperties} funcione sem a necessidade de escanear
 * explicitamente na classe principal da aplicação. Isso promove maior encapsulamento.
 *
 * @param secretKey A chave secreta usada para assinar e verificar os tokens JWT.
 * @param expirationHours A duração da validade de um token, em horas.
 * @param issuer O identificador (emissor) do token, que neste caso é a "upgeek-api".
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        long expirationHours,
        String issuer
) {}
