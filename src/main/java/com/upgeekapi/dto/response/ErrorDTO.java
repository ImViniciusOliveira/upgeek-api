package com.upgeekapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO padronizado para respostas de erro da API.
 * Pode conter uma mensagem de erro geral e, opcionalmente, um mapa
 * de erros específicos de campo para falhas de validação.
 */
@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Garante que campos nulos (como fieldErrors) não apareçam no JSON
public class ErrorDTO {

        private final Instant timestamp;
        private final int status;
        private final String error;
        private final String message;
        private Map<String, String> fieldErrors; // Novo campo para erros de validação

        /**
         * Construtor adicional para o caso específico de erros de validação.
         * @param fieldErrors O mapa de erros de campo.
         */
        public ErrorDTO(Instant timestamp, int status, String error, String message, Map<String, String> fieldErrors) {
                this(timestamp, status, error, message);
                this.fieldErrors = fieldErrors;
        }
}