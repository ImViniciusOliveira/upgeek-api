package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Representa uma estrutura de resposta de erro padronizada para a API,
 * garantindo que os erros sejam comunicados de forma consistente ao cliente.
 */
@Schema(description = "Objeto padronizado para respostas de erro da API.")
public record ErrorDTO(

        @Schema(description = "O momento exato em que o erro ocorreu, em formato UTC.",
                example = "2025-06-26T03:30:00.123Z")
        Instant timestamp,

        @Schema(description = "O código de status HTTP que representa o erro.",
                example = "404")
        Integer status,

        @Schema(description = "A descrição textual do status HTTP.",
                example = "Not Found")
        String error,

        @Schema(description = "A mensagem detalhada e legível que descreve a causa específica do erro.",
                example = "Recurso 'Usuário' com o identificador 'ninguem@email.com' não foi encontrado.")
        String message
) {}