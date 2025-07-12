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
 * Anotação de composição para validar um termo de busca.
 * Garante que o termo, se presente, não contenha espaços no início ou no fim
 * e que respeite um limite de tamanho para evitar queries maliciosas ou ineficientes.
 */
@Size(max = 100, message = "O termo de busca por nome não pode exceder 100 caracteres.")
@Pattern(regexp = "^$|^\\S(?:.*\\S)?$", message = "O termo de busca não pode conter espaços no início ou no fim.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSearchTerm {
    String message() default "Termo de busca inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}