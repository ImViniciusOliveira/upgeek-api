package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ValidEmailFormat;
import com.upgeekapi.validation.annotation.ValidNameFormat;
import com.upgeekapi.validation.annotation.ValidUsername;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados que um usuário pode atualizar em sua própria conta.
 * <p>
 * Como todos os campos são opcionais, o serviço de atualização deve tratar
 * apenas os campos que não forem nulos no payload da requisição.
 */
@Schema(description = "Dados permitidos para atualização na conta do usuário. Todos os campos são opcionais; envie apenas os que deseja alterar.")
public record UpdateAccountRequestDTO(

        @ValidUsername
        @Schema(description = "O novo nome de usuário único na plataforma.", example = "Kain Prime")
        String username,

        @ValidNameFormat
        @Schema(description = "O novo nome de exibição público para o usuário.", example = "João O'Malley")
        String name,

        @ValidEmailFormat
        @Schema(description = "O novo email para login e contato.", example = "kain.prime@upgeek.com")
        String email
) {}