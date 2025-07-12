package com.upgeekapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de composição para validar o formato de um nome completo de pessoa.
 * <p>
 * Garante que, se o nome for fornecido, ele respeite o limite de tamanho e a
 * whitelist de caracteres. Não valida a presença, sendo ideal para campos opcionais.
 */
@Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
@Pattern(
        regexp = "^[\\p{L} .'-]+(?: [\\p{L} .'-]+)*$",
        message = "O nome completo contém caracteres inválidos ou espaços no início/fim."
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNameFormat {
    String message() default "Formato de nome inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}