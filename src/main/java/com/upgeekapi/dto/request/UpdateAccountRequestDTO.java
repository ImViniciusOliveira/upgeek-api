package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados que um usuário pode atualizar em sua própria conta.
 */
@Schema(description = "Dados permitidos para atualização na conta do usuário. Todos os campos são opcionais; envie apenas os que deseja alterar.")
public record UpdateAccountRequestDTO(

        @Size(min = 3, max = 16, message = "O nome de usuário deve ter entre 3 e 16 caracteres.")
        // Esta regex já proíbe espaços no início/fim
        @Pattern(regexp = "^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*$", message = "O nome de usuário contém caracteres inválidos ou espaços no início/fim.")
        @Schema(description = "O novo nome de usuário único na plataforma.", example = "Kain Prime")
        String username,

        @Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
        // Esta regex já proíbe espaços no início/fim
        @Pattern(regexp = "^[\\p{L} .'-]+(?: [\\p{L} .'-]+)*$", message = "O nome completo contém caracteres inválidos ou espaços no início/fim.")
        @Schema(description = "O novo nome de exibição público para o usuário.", example = "João O'Malley")
        String name,

        @Email(message = "O formato do email é inválido.")
        @Size(max = 255, message = "O email não pode exceder 255 caracteres.")
        // ADICIONADO: Validação para proibir espaços no início/fim
        @Pattern(regexp = "^\\S.*\\S$", message = "O email não pode conter espaços no início ou no fim.")
        @Schema(description = "O novo email para login e contato.", example = "kain.prime@upgeek.com")
        String email
) {}