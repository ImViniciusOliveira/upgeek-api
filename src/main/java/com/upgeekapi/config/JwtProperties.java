package com.upgeekapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mapeia as configurações de JWT do arquivo {@code application.yml} para um objeto
 * Java imutável e "type-safe".
 * <p>
 * Esta classe utiliza o padrão "Properties Holder", atuando como um contêiner de dados
 * simples e seguro. O uso de um {@code record} garante a imutabilidade, uma
 * prática recomendada para dados de configuração.
 * <p>
 * <strong>Nota de Design:</strong> Esta classe intencionalmente <strong>não</strong> é
 * anotada com {@code @Component} ou {@code @Configuration}. Ela é ativada e registrada
 * como um bean através da anotação {@code @EnableConfigurationProperties} em uma classe
 * de configuração separada ({@link AppConfig}). Essa abordagem desacopla a configuração
 * e previne problemas de dependência circular com a {@link SecurityConfig}.
 *
 * @param secretKey A chave secreta utilizada para assinar e verificar os tokens JWT.
 *                  Por segurança, este valor deve ser fornecido via variável de ambiente.
 * @param expirationHours A duração da validade de um token, em horas.
 * @param issuer O identificador (emissor) do token, conforme o padrão JWT (claim "iss").
 *               Identifica qual sistema emitiu o token.
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        long expirationHours,
        String issuer
) {}