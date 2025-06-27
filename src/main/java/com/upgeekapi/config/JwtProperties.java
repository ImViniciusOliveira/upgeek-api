package com.upgeekapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mapeia as configurações do JWT do arquivo 'application.properties' para um objeto Java.
 * Esta é uma prática de Clean Code que garante segurança de tipos (type-safety)
 * e centraliza a configuração, evitando "strings mágicas" espalhadas pelo código.
 * @param secretKey A chave secreta usada para assinar os tokens.
 * @param expirationHours A duração da validade de um token em horas.
 * @param issuer O emissor (quem está gerando o token), que no nosso caso é a "upgeek-api".
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        long expirationHours,
        String issuer
) {}