package com.upgeekapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.upgeekapi.config.jackson.StringTrimDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Centraliza a configuração do {@link ObjectMapper} do Jackson.
 * <p>
 * Esta classe garante que todos os processos de serialização e desserialização JSON
 * em toda a aplicação se comportem de forma consistente, registrando módulos customizados
 * para sanitização de dados e tratamento adequado de tipos de dados modernos do Java.
 */
@Configuration
public class JacksonConfig {

    /**
     * Cria e configura o bean {@link ObjectMapper} primário para a aplicação.
     * <p>
     * A anotação {@code @Primary} designa este bean como a escolha padrão para
     * injeção de dependência, sobrescrevendo o ObjectMapper padrão fornecido pelo Spring Boot.
     * Esta instância é aprimorada com dois módulos chave:
     * <ul>
     *     <li>{@link JavaTimeModule}: Habilita a serialização/desserialização correta de objetos
     *     da API de Data e Hora do Java 8 (ex: {@code Instant}, {@code LocalDate}) para o formato padrão ISO-8601.</li>
     *     <li>Um módulo customizado com {@link StringTrimDeserializer}: Sanitiza automaticamente todos os dados de
     *     string recebidos, removendo espaços em branco no início e no fim.</li>
     * </ul>
     *
     * @return Uma instância do {@code ObjectMapper} totalmente configurada.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Registra o módulo que fornece suporte para os tipos da API de Data e Hora do Java 8.
        objectMapper.registerModule(new JavaTimeModule());

        // Registra um módulo customizado para sanitização de dados.
        SimpleModule trimModule = new SimpleModule("StringTrimModule");
        trimModule.addDeserializer(String.class, new StringTrimDeserializer());
        objectMapper.registerModule(trimModule);

        return objectMapper;
    }
}