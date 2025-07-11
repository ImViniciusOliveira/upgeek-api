package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para a requisição de atualização de senha.
 * <p>
 * Este objeto encapsula a senha atual para verificação e a nova senha,
 * que deve atender a todos os critérios de complexidade definidos.
 */
@Schema(description = "Dados necessários para a atualização de senha do usuário.")
public record UpdatePasswordRequestDTO(

        @NotBlank(message = "A senha atual é obrigatória.")
        @Pattern(regexp = "^\\S*$", message = "A senha atual não pode conter espaços.")
        @Schema(description = "A senha atual do usuário para verificação.", example = "senhaForte123!", requiredMode = Schema.RequiredMode.REQUIRED)
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 12, max = 72, message = "A nova senha deve ter entre 12 e 72 caracteres.")
        @Pattern(regexp = "^\\S*$", message = "A nova senha não pode conter espaços.")
        @Pattern(regexp = ".*[a-z].*", message = "A nova senha deve conter pelo menos uma letra minúscula.")
        @Pattern(regexp = ".*[A-Z].*", message = "A nova senha deve conter pelo menos uma letra maiúscula.")
        @Pattern(regexp = ".*\\d.*", message = "A nova senha deve conter pelo menos um número.")
        @Pattern(regexp = ".*[@$!%*?&#._-].*", message = "A nova senha deve conter pelo menos um caractere especial.")
        @Schema(description = "A nova senha para a conta. Deve atender aos critérios de complexidade.", example = "NovaSenhaSuperSegura#456", requiredMode = Schema.RequiredMode.REQUIRED)
        String newPassword
) {}