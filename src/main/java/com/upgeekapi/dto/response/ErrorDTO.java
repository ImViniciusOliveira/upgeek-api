package com.upgeekapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO padronizado e imutável para respostas de erro da API.
 * Pode conter uma mensagem de erro geral e, opcionalmente, um mapa
 * de erros específicos de campo para falhas de validação.
 */
@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

        private final Instant timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final Map<String, String> fieldErrors;

        /**
         * Construtor para erros que não possuem detalhes de validação de campo.
         * Um atalho conveniente que chama o construtor principal.
         */
        public ErrorDTO(Instant timestamp, int status, String error, String message) {
                this(timestamp, status, error, message, null);
        }
}