package com.upgeekapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração central para a aplicação.
 * <p>
 * Sua principal responsabilidade é hospedar beans de utilidade geral e configurações
 * transversais que são necessárias em múltiplos módulos, como o {@link PasswordEncoder}.
 * Manter esses beans aqui é uma decisão de design estratégica para desacoplar
 * configurações e quebrar dependências circulares, especialmente com a {@link SecurityConfig}.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AppConfig {

    /**
     * Define o bean do codificador de senhas para ser utilizado em toda a aplicação.
     * <p>
     * Ao declarar este bean em uma classe de configuração separada, quebramos a
     * dependência circular que ocorreria se ele estivesse definido na {@code SecurityConfig}.
     * Isso garante que o {@code PasswordEncoder} seja criado antes de qualquer componente
     * de segurança que dependa dele, tornando o processo de inicialização do Spring
     * mais robusto e previsível.
     *
     * @return Uma instância do {@link BCryptPasswordEncoder}, o padrão recomendado para hashing de senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}