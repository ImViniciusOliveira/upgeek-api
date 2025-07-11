package com.upgeekapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
// Importe o módulo de data/hora do Java 8
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.upgeekapi.config.jackson.StringTrimDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuração customizada para o ObjectMapper do Jackson.
 */
@Configuration
public class JacksonConfig {

    /**
     * Cria e configura um ObjectMapper primário para a aplicação.
     * Este bean será usado por padrão pelo Spring Boot para toda a
     * serialização e deserialização de JSON.
     *
     * @return Uma instância do ObjectMapper com as customizações da aplicação.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // **A CORREÇÃO ESTÁ AQUI**
        // Registra o módulo que ensina o Jackson a lidar com Instant, LocalDate, etc.
        objectMapper.registerModule(new JavaTimeModule());

        // Agora, adiciona a nossa customização de trim
        SimpleModule trimModule = new SimpleModule();
        trimModule.addDeserializer(String.class, new StringTrimDeserializer());
        objectMapper.registerModule(trimModule);

        return objectMapper;
    }
}