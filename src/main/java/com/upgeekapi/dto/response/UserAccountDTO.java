package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados detalhados da conta do usuário")
public record UserAccountDTO(
        @Schema(description = "ID interno do usuário no banco de dados (UUID).", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Email do usuário.", example = "usuario@email.com")
        String email,

        @Schema(description = "Nome de exibição do usuário.", example = "Usuário Geek")
        String name,

        @Schema(description = "Nível atual do usuário no sistema de gamificação.", example = "5")
        int level,

        @Schema(description = "Pontos de experiência (XP) acumulados pelo usuário.", example = "4500")
        long xp,

        @Schema(description = "Título do usuário baseado no seu nível de gamificação.", example = "Colecionador Lendário")
        String title
) {}
