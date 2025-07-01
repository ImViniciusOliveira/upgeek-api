package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados que um usuário pode atualizar em sua própria conta.
 * <p>
 * Propositalmente, apenas campos não-sensíveis são incluídos. A alteração de dados
 * críticos como email, CPF ou senha deve ter seus próprios endpoints e fluxos de
 * segurança mais robustos (ex: confirmação por email).
 */
@Schema(description = "Dados permitidos para atualização na conta do usuário.")
public record UpdateAccountRequestDTO(

        @Size(min = 3, max = 16, message = "O nome de usuário deve ter entre 3 e 16 caracteres.")
        @Pattern(regexp = "^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*$", message = "O nome de usuário deve conter apenas letras e números, e não pode começar ou terminar com espaços.")
        @Schema(description = "O novo nome de usuário único na plataforma.", example = "Kain O Renegado")
        String username,

        @Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O nome completo deve conter apenas letras e espaços.")
        @Schema(description = "O novo nome de exibição público para o usuário.", example = "Kain, o Caçador de Relíquias")
        String name,

        @Email(message = "O formato do email é inválido.")
        @Size(max = 255, message = "O email não pode exceder 255 caracteres.")
        @Schema(description = "O novo email para login e contato.", example = "kain.hunter@duum.net")
        String email

) {}