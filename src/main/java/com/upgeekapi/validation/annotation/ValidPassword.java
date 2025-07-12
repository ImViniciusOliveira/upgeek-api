package com.upgeekapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de composição que agrupa todas as regras de validação para uma senha.
 * Garante que todas as senhas na aplicação sigam um padrão de complexidade consistente,
 * mantendo os DTOs limpos e legíveis. Cada regra interna mantém sua própria mensagem de erro.
 */
@NotBlank(message = "A senha é obrigatória.")
@Size(min = 12, max = 72, message = "A senha deve ter entre 12 e 72 caracteres.")
@Pattern(regexp = "^\\S*$", message = "A senha não pode conter espaços.")
@Pattern(regexp = ".*[a-z].*", message = "A senha deve conter pelo menos uma letra minúscula.")
@Pattern(regexp = ".*[A-Z].*", message = "A senha deve conter pelo menos uma letra maiúscula.")
@Pattern(regexp = ".*\\d.*", message = "A senha deve conter pelo menos um número.")
@Pattern(regexp = ".*[@$!%*?&#._-].*", message = "A senha deve conter pelo menos um caractere especial.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Senha inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}