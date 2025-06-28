package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados públicos e de gamificação de uma conta de usuário.
 * Usado para exibir informações de perfil no frontend, como na página de conta ou na navbar.
 */
@Schema(description = "Dados detalhados da conta de um usuário para exibição.")
public record UserAccountDTO(

        @Schema(description = "O identificador único do usuário no sistema.",
                example = "1")
        Long id,

        @Schema(description = "O nome de usuário público.",
                example = "kain_renegade")
        String username,

        @Schema(description = "O nome completo da pessoa.",
                example = "Kain")
        String name,

        @Schema(description = "O endereço de email associado à conta.",
                example = "kain.renegade@duum.net")
        String email,

        @Schema(description = "O nível atual do usuário no sistema de gamificação.",
                example = "7")
        int level,

        @Schema(description = "A quantidade total de pontos de experiência (XP) do usuário.",
                example = "8800")
        long xp,

        @Schema(description = "O título atual do usuário, baseado em seu nível ou conquistas.",
                example = "Colecionador Nível 7")
        String title
) {}