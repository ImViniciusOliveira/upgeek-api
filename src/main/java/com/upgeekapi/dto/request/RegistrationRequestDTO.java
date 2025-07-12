package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ValidCPF;
import com.upgeekapi.validation.annotation.ValidEmail;
import com.upgeekapi.validation.annotation.ValidPassword;
import com.upgeekapi.validation.annotation.ValidPersonName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para o registro de um novo usuário.")
public record RegistrationRequestDTO(

        @ValidPersonName
        @Schema(description = "Nome completo da pessoa.", example = "Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @ValidEmail
        @Schema(description = "Email único para login e contato.", example = "lira.valen@scarlate.org", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @ValidCPF
        @Schema(description = "CPF do usuário.", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
        String cpf,

        @ValidPassword
        @Schema(description = "A senha para a nova conta.", example = "AliancaScarlate#123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}