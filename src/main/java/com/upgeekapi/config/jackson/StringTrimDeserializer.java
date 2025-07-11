package com.upgeekapi.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Um deserializer customizado para o Jackson que remove automaticamente espaços em branco
 * do início e do fim de qualquer campo do tipo String durante o processo de conversão
 * de JSON para objeto Java.
 * <p>
 * Esta abordagem garante a consistência dos dados, prevenindo que espaços em branco
 * acidentais sejam persistidos no banco de dados ou utilizados na lógica de negócio.
 * É tipicamente registrado globalmente na configuração do Jackson para ser aplicado
 * em toda a aplicação.
 */
public class StringTrimDeserializer extends JsonDeserializer<String> {

    /**
     * Deserializa um valor de string do JSON, aplicando a remoção de espaços em branco.
     *
     * @param p O parser que lê o conteúdo JSON.
     * @param ctxt O contexto da deserialização, que pode ser usado para acessar configurações.
     * @return A string original sem espaços em branco no início ou no fim, ou {@code null} se o valor original for nulo.
     * @throws IOException se ocorrer um erro de I/O durante a leitura do JSON.
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return value == null ? null : value.trim();
    }
}