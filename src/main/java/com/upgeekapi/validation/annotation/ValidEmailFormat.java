package com.upgeekapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de composição para validar o formato de um endereço de email.
 * <p>
 * Garante que, se o email for fornecido, ele tenha um formato válido e respeite
 * o limite de tamanho. Não valida a presença, sendo ideal para campos opcionais.
 */
@Email(message = "O formato do email é inválido.")
@Size(max = 255, message = "O email não pode exceder 255 caracteres.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailFormat {
    String message() default "Formato de email inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}