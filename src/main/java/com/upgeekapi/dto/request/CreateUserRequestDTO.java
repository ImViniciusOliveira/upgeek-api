package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados necessários para criar uma nova conta de usuário.
 * Usado como corpo da requisição no endpoint de criação de teste.
 */
@Schema(description = "Dados necessários para registrar um novo usuário (para fins de teste).")
public record CreateUserRequestDTO(

        @Schema(description = "O endereço de email único para o novo usuário.",
                example = "novo.colecionador@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "O nome de exibição público para o novo usuário.",
                example = "Novo Colecionador",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) {}