package com.upgeekapi.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Deserializer customizado para o Jackson que remove espaços em branco
 * do início e do fim de qualquer campo do tipo String durante a
 * conversão do JSON para o objeto Java.
 */
public class StringTrimDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Obtém o valor original do JSON e aplica o trim()
        String value = p.getValueAsString();
        return value == null ? null : value.trim();
    }
}