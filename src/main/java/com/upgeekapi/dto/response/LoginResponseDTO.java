package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a resposta de um login bem-sucedido, contendo o token de acesso.
 */
@Schema(description = "Resposta de uma autenticação bem-sucedida.")
public record LoginResponseDTO(
        @Schema(description = "O token de acesso JWT (Bearer Token).")
        String token
) {}