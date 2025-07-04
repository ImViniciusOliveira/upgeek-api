package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO que representa os dados de credenciais para uma tentativa de login.
 * Usado como o corpo da requisição (payload) no endpoint de autenticação.
 */
@Schema(description = "Credenciais necessárias para autenticação.")
public record LoginRequestDTO(

        @NotBlank(message = "O email é obrigatório.")
        @Schema(description = "O email do usuário para autenticação.",
                example = "kain.admin@upgeek.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Schema(description = "A senha associada à conta do usuário.",
                example = "AdminLegacy#7890",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}