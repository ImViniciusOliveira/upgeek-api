package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados que um usuário pode atualizar em sua própria conta.
 * <p>
 * Propositalmente, apenas campos não-sensíveis são incluídos. A alteração de dados
 * críticos como email, CPF ou senha deve ter seus próprios endpoints e fluxos de
 * segurança mais robustos (ex: confirmação por email).
 */
@Schema(description = "Dados permitidos para atualização na conta do usuário.")
public record UpdateAccountRequestDTO(

        @Schema(description = "O novo nome de exibição público para o usuário.",
                example = "Kain, o Renegado de Duum")
        String name
) {}