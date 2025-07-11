package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email; // Import da anotação @Email
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO que representa os dados de credenciais para uma tentativa de login.
 * Usado como o corpo da requisição (payload) no endpoint de autenticação.
 */
@Schema(description = "Credenciais necessárias para autenticação.")
public record LoginRequestDTO(

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        @Schema(description = "O email do usuário para autenticação.",
                example = "kain.admin@upgeek.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Pattern(regexp = "^\\S.*\\S$|^\\S*$", message = "A senha não pode conter espaços no início ou no fim.")
        @Schema(description = "A senha associada à conta do usuário.",
                example = "AdminLegacy#7890",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}