package com.upgeekapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração central para o Spring Security.
 * Define as regras de acesso, filtros de segurança e outras políticas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF, pois nossa API é stateless (não usa sessões/cookies para autenticação)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Define a política de sessão como STATELESS, essencial para APIs REST com JWT/Bearer Token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para cada endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // A REGRA PRINCIPAL: Permite acesso PÚBLICO a todos os nossos endpoints de teste
                        .requestMatchers("/api/account/test/**", "/api/account/all").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Libera o Swagger

                        // Para QUALQUER OUTRA requisição, exija autenticação
                        .anyRequest().authenticated()
                );

        // TODO: Futuramente, adicionaremos aqui o nosso filtro de autenticação do Auth0.

        return http.build();
    }
}
