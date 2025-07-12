package com.upgeekapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de composição para validar um endereço de email.
 * Agrupa as regras de não-nulidade, formato de email e limite de tamanho.
 */
@NotBlank(message = "O email é obrigatório.")
@Email(message = "O formato do email é inválido.")
@Size(max = 255, message = "O email não pode exceder 255 caracteres.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Email inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}