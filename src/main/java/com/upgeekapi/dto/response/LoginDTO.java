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
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
        String token
) {}
