package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a resposta de uma autenticação bem-sucedida.
 * Contém o token de acesso JWT que o cliente deve usar para
 * autorizar requisições subsequentes.
 */
@Schema(description = "Resposta de uma autenticação bem-sucedida, contendo o token de acesso.")
public record LoginDTO(

        @Schema(
                description = "O token de acesso JWT (Bearer Token) que deve ser enviado no cabeçalho 'Authorization' em requisições protegidas.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImVtYWlsIjoia2Fpbi5yZW5lZ2FkZUBkdXVtLm5ldCIsImlhdCI6MTcxOTk3..."
        )
        String token
) {}