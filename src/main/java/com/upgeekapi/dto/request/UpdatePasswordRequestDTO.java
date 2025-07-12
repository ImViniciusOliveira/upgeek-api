package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ProvidedPassword;
import com.upgeekapi.validation.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para a requisição de atualização de senha.
 * <p>
 * Este objeto encapsula a senha atual para verificação e a nova senha,
 * que deve atender a todos os critérios de complexidade definidos.
 */
@Schema(description = "Dados necessários para a atualização de senha do usuário.")
public record UpdatePasswordRequestDTO(

        @ProvidedPassword
        @Schema(description = "A senha atual do usuário para verificação.",
                example = "senhaForte123!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String currentPassword,

        @ValidPassword
        @Schema(description = "A nova senha para a conta. Deve atender aos critérios de complexidade.",
                example = "NovaSenhaSuperSegura#456",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String newPassword
) {}