package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados de credenciais para uma tentativa de login.
 */
@Schema(description = "Credenciais necessárias para autenticação.")
public record LoginRequestDTO(
        @Schema(description = "Email do usuário.", example = "geek.master@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "Senha do usuário.", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}