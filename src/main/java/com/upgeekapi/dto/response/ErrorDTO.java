package com.upgeekapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * DTO padronizado e imutável para respostas de erro da API, implementado como um record.
 * <p>
 * Esta estrutura garante que todas as respostas de erro sigam um formato consistente,
 * facilitando o tratamento de erros pelo cliente. A anotação {@code @JsonInclude(JsonInclude.Include.NON_NULL)}
 * otimiza o payload, omitindo o mapa {@code fieldErrors} quando não houver erros de validação.
 *
 * @param timestamp   O momento exato em que o erro ocorreu.
 * @param status      O código de status HTTP (ex: 400, 404, 500).
 * @param error       Uma breve descrição do status HTTP (ex: "Bad Request", "Not Found").
 * @param message     Uma mensagem legível por humanos explicando a causa do erro.
 * @param fieldErrors Um mapa opcional contendo erros de validação específicos por campo.
 *                    Será nulo para erros que não são de validação.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ErrorResponse", description = "Representação padronizada de uma resposta de erro da API.")
public record ErrorDTO(
        @Schema(description = "O momento exato em que o erro ocorreu.", example = "2025-07-10T23:54:06.108Z")
        Instant timestamp,

        @Schema(description = "O código de status HTTP.", example = "400")
        int status,

        @Schema(description = "A descrição textual do status HTTP.", example = "Bad Request")
        String error,

        @Schema(description = "Uma mensagem detalhada e legível sobre o erro.", example = "A validação da requisição falhou.")
        String message,

        @Schema(description = "Um mapa de erros de validação específicos por campo. Presente apenas para erros de validação (status 400).",
                example = "{\"fieldName\": \"A mensagem de erro específica para este campo.\"}")
        Map<String, String> fieldErrors
) {
        /**
         * Construtor de conveniência para erros que não estão relacionados à validação de campos,
         * como 'Not Found' (404) ou 'Internal Server Error' (500).
         *
         * @param timestamp O momento do erro.
         * @param status    O código de status HTTP.
         * @param error     A descrição do status.
         * @param message   A mensagem de erro.
         */
        public ErrorDTO(Instant timestamp, int status, String error, String message) {
                this(timestamp, status, error, message, null);
        }
}