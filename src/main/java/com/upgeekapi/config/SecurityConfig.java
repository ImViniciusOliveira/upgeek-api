package com.upgeekapi.config;

import com.upgeekapi.security.CustomAccessDeniedHandler;
import com.upgeekapi.security.CustomAuthenticationEntryPoint;
import com.upgeekapi.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configura a camada de segurança da aplicação utilizando Spring Security.
 * <p>
 * Esta classe é o ponto central para definir as políticas de segurança, incluindo:
 * <ul>
 *     <li>Gerenciamento de sessão stateless, ideal para APIs RESTful com JWT.</li>
 *     <li>Regras de autorização granulares para cada endpoint.</li>
 *     <li>Integração de um filtro de autenticação JWT customizado.</li>
 *     <li>Registro de handlers para respostas de erro de segurança padronizadas (401 e 403).</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Define a cadeia de filtros de segurança que intercepta todas as requisições HTTP.
     * <p>
     * A configuração desabilita funcionalidades legadas (CSRF, form login), impõe a política
     * de sessão stateless, define as regras de acesso para os endpoints e insere o
     * {@link JwtAuthenticationFilter} no pipeline de segurança.
     *
     * @param http O objeto {@link HttpSecurity} para configurar a segurança web.
     * @return A {@link SecurityFilterChain} construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita a proteção CSRF, pois não usamos sessões baseadas em cookies.
                .csrf(AbstractHttpConfigurer::disable)
                // Desabilita o formulário de login e a autenticação básica padrão do Spring.
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Define a política de sessão como STATELESS, garantindo que cada requisição seja autenticada de forma independente.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura os handlers para erros de autenticação (401) e autorização (403).
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Define as regras de autorização para cada endpoint da API.
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos, acessíveis sem autenticação.
                        .requestMatchers("/api/v1").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Endpoints que exigem a permissão de ADMIN.
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Endpoints que exigem a permissão de USER.
                        .requestMatchers("/api/v1/account/**").hasRole("USER")
                        .requestMatchers("/api/auth/me").hasRole("USER")

                        // Qualquer outra requisição não listada acima exige autenticação.
                        .anyRequest().authenticated()
                )
                // Adiciona nosso filtro JWT customizado antes do filtro padrão de autenticação.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expõe o {@link AuthenticationManager} do Spring como um Bean gerenciável.
     * <p>
     * Este passo é crucial para a arquitetura. Ao expor o AuthenticationManager,
     * permitimos que outros componentes, como o {@code AuthService}, o injetem para
     * processar as tentativas de login de forma explícita. Sem este bean, o Spring
     * consideraria a configuração de segurança incompleta e ativaria o modo padrão
     * com a senha gerada no console, que é o comportamento que queríamos evitar.
     *
     * @param configuration O objeto de configuração de autenticação fornecido pelo Spring.
     * @return O {@link AuthenticationManager} configurado.
     * @throws Exception se houver um erro ao obter o manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}