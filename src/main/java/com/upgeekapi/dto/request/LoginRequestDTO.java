package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados de credenciais para uma tentativa de login.
 * Usado como o corpo da requisição (payload) no endpoint de autenticação.
 */
@Schema(description = "Credenciais necessárias para autenticação.")
public record LoginRequestDTO(

        @Schema(description = "O email do usuário para autenticação.",
                example = "kain.renegade@duum.net",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "A senha associada à conta do usuário.",
                example = "darkligthside123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}
