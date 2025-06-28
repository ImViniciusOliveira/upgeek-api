package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO (Data Transfer Object) que representa os dados necessários para registrar uma nova conta de usuário.
 * Usado como o corpo da requisição (payload) no endpoint de registro.
 */
@Schema(description = "Dados necessários para o registro de um novo usuário.")
public record RegistrationRequestDTO(

        @Schema(description = "Nome de usuário único na plataforma (sem espaços ou caracteres especiais).",
                example = "kain_renegade",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @Schema(description = "Nome completo da pessoa, para fins de faturamento e envio.",
                example = "Kain",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Email único para login e contato.",
                example = "kain.renegade@duum.net",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "CPF do usuário (apenas números), necessário para transações.",
                example = "98765432100",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String cpf,

        @Schema(description = "A senha para a nova conta. Deve atender aos requisitos de segurança.",
                example = "darkligthside123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}
