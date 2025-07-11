package com.upgeekapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa os dados públicos e de gamificação de uma conta de usuário.
 * <p>
 * Este record imutável é projetado para ser uma representação "plana" dos dados
 * de um usuário, ideal para ser exibido em perfis ou retornado em operações
 * de gerenciamento de conta.
 *
 * @param id       O identificador único do usuário no sistema.
 * @param username O nome de usuário público, usado para identificação na plataforma.
 * @param name     O nome completo da pessoa.
 * @param email    O endereço de email associado à conta.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UserAccountData", description = "Dados detalhados da conta de um usuário para exibição.")
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
        String email
) {}