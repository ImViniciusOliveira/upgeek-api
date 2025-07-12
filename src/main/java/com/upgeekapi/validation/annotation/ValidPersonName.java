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
 * Anotação de composição para validar um nome completo de pessoa.
 * Agrupa as regras de não-nulidade, tamanho e a whitelist de caracteres
 * permitidos para nomes (letras de qualquer idioma, espaços, ', . e -).
 */
@NotBlank(message = "O nome completo é obrigatório.")
@Size(max = 100, message = "O nome completo não pode exceder 100 caracteres.")
@Pattern(regexp = "^[\\p{L} .'-]+(?: [\\p{L} .'-]+)*$", message = "O nome completo contém caracteres inválidos ou espaços no início/fim.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonName {
    String message() default "Nome de pessoa inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}