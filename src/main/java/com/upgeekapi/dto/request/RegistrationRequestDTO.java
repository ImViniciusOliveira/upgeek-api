package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO que representa os dados para registrar um novo usuário, com regras de validação robustas.
 */
@Schema(description = "Dados necessários para o registro de um novo usuário.")
public record RegistrationRequestDTO(

        @NotBlank(message = "O nome de usuário é obrigatório.")
        @Size(min = 3, max = 16, message = "O nome de usuário deve ter entre 3 e 16 caracteres.")
        @Pattern(regexp = "^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*$", message = "O nome de usuário deve conter apenas letras e números, e não pode começar ou terminar com espaços.")
        @Schema(description = "Nome de usuário único na plataforma.", example = "Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @NotBlank(message = "O nome completo é obrigatório.")
        @Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "O nome completo contém caracteres inválidos.")
        @Schema(description = "Nome completo da pessoa.", example = "Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        @Size(max = 255, message = "O email não pode exceder 255 caracteres.")
        @Schema(description = "Email único para login e contato.", example = "lira.valen@scarlate.org", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "O CPF é obrigatório.")
        @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
        @Pattern(regexp = "^[0-9]+$", message = "O CPF deve conter apenas números.")
        @Schema(description = "CPF do usuário (apenas números).", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
        String cpf,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 12, max = 72, message = "A senha deve ter entre 12 e 72 caracteres.")
        @Pattern(regexp = ".*[a-z].*", message = "A senha deve conter pelo menos uma letra minúscula.")
        @Pattern(regexp = ".*[A-Z].*", message = "A senha deve conter pelo menos uma letra maiúscula.")
        @Pattern(regexp = ".*\\d.*", message = "A senha deve conter pelo menos um número.")
        @Pattern(regexp = ".*[@$!%*?&#._-].*", message = "A senha deve conter pelo menos um caractere especial.")
        @Schema(description = "A senha para a nova conta.", example = "AliancaScarlate#123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}