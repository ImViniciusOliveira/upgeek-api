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
 * Anotação de composição para validar a URL de uma imagem.
 * Garante que o campo não seja vazio e não contenha espaços.
 */
@NotBlank(message = "A URL da imagem é obrigatória.")
@Pattern(regexp = "^\\S+$", message = "A URL da imagem não pode conter espaços.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageUrl {
    String message() default "URL da imagem inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}