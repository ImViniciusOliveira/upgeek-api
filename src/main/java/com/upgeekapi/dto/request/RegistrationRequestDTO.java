package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

/**
 * DTO que representa os dados para registrar um novo usuário.
 * O username é gerado automaticamente a partir do email no momento do registro.
 */
@Schema(description = "Dados necessários para o registro de um novo usuário.")
public record RegistrationRequestDTO(

        @NotBlank(message = "O nome completo é obrigatório.")
        @Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
        @Pattern(regexp = "^[\\p{L}.'-]+(?: [\\p{L}.'-]+)*$", message = "O nome completo contém caracteres inválidos ou espaços no início/fim.")
        @Schema(description = "Nome completo da pessoa.", example = "Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        @Size(max = 255, message = "O email não pode exceder 255 caracteres.")
        @Pattern(regexp = "^\\S.*\\S$|^\\S*$", message = "O email não pode conter espaços no início ou no fim.")
        @Schema(description = "Email único para login e contato. Será usado para gerar um nome de usuário inicial.", example = "lira.valen@scarlate.org", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "O CPF é obrigatório.")
        @CPF(message = "O CPF fornecido é inválido.")
        @Pattern(regexp = "^\\S.*\\S$|^\\S*$", message = "O CPF não pode conter espaços no início ou no fim.")
        @Schema(description = "CPF do usuário (pode ser formatado ou apenas números).", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
        String cpf,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 12, max = 72, message = "A senha deve ter entre 12 e 72 caracteres.")
        @Pattern(regexp = "^\\S.*\\S$|^\\S*$", message = "A senha não pode conter espaços no início ou no fim.")
        @Pattern(regexp = ".*[a-z].*", message = "A senha deve conter pelo menos uma letra minúscula.")
        @Pattern(regexp = ".*[A-Z].*", message = "A senha deve conter pelo menos uma letra maiúscula.")
        @Pattern(regexp = ".*\\d.*", message = "A senha deve conter pelo menos um número.")
        @Pattern(regexp = ".*[@$!%*?&#._-].*", message = "A senha deve conter pelo menos um caractere especial.")
        @Schema(description = "A senha para a nova conta. Deve atender aos critérios de complexidade.", example = "AliancaScarlate#123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}