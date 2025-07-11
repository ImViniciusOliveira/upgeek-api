package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a resposta de uma autenticação bem-sucedida.
 * <p>
 * Este record imutável encapsula o token de acesso JWT, que é a "chave"
 * que o cliente deve usar para se autenticar em endpoints protegidos da API.
 *
 * @param token O token de acesso JWT (Bearer Token) gerado.
 */
@Schema(name = "LoginResponse", description = "Resposta de uma autenticação bem-sucedida, contendo o token de acesso.")
public record LoginDTO(

        @Schema(
                description = "O token de acesso JWT (Bearer Token) que deve ser enviado no cabeçalho 'Authorization' em requisições protegidas.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ1cGdlZWstYXBpIiwic3ViIjoiMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NTIyMDQ0NzgsImV4cCI6MTc1MjI5MDg3OH0.KQun-c1yo0RMbPOIgC4doJJjd4gfOzfxfE5QCrZgXvg"
        )
        String token
) {}