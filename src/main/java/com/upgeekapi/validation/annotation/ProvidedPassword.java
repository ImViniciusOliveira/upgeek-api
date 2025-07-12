package com.upgeekapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de composição para validar uma senha que está sendo fornecida para verificação.
 * <p>
 * Garante que a senha não seja vazia e não contenha espaços.
 */
@NotBlank(message = "A senha é obrigatória.")
@Pattern(regexp = "^\\S*$", message = "A senha não pode conter espaços.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidedPassword {
    String message() default "Senha fornecida inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}