package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Representa uma estrutura de resposta de erro padronizada para a API.
 * Garante que todos os erros, sejam eles de negócio ou de sistema, sejam
 * comunicados ao cliente de forma consistente e informativa.
 */
@Schema(description = "Objeto padronizado para respostas de erro da API.")
public record ErrorDTO(

        @Schema(description = "O momento exato em que o erro ocorreu, em formato UTC (ISO 8601).",
                example = "2025-06-28T18:15:00.123Z")
        Instant timestamp,

        @Schema(description = "O código de status HTTP que representa o erro.",
                example = "404")
        Integer status,

        @Schema(description = "A descrição textual padrão para o status HTTP.",
                example = "Not Found")
        String error,

        @Schema(description = "A mensagem detalhada e legível que descreve a causa específica do erro.",
                example = "Usuário com o email 'kain.renegade@duum.net' não foi encontrado.")
        String message
) {}
