package com.upgeekapi.config;

import com.upgeekapi.security.CustomAuthenticationEntryPoint;
import com.upgeekapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração central do Spring Security.
 * Define as regras de acesso da API, desabilita mecanismos de segurança baseados
 * em sessão e integra o filtro de autenticação JWT customizado.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF, pois a API é stateless.
                .csrf(AbstractHttpConfigurer::disable)

                // A CORREÇÃO: Desabilita o formulário de login e a autenticação básica padrão do Spring.
                // Isso remove a senha gerada automaticamente no console.
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Define a política de sessão como STATELESS, essencial para APIs REST com JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura o handler para erros de autenticação (401).
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )

                // Define as regras de autorização para cada endpoint.
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Endpoints que exigem autenticação e a role "USER"
                        .requestMatchers("/api/account/**").hasRole("USER")
                        .requestMatchers("/api/auth/me").hasRole("USER")

                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                // Adiciona nosso filtro JWT na cadeia de segurança.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean para o codificador de senhas. Usa BCrypt, o padrão da indústria.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
