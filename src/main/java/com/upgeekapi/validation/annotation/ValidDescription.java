
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
 * Anotação de composição para validar um campo de descrição.
 * Garante que a descrição, se presente, não contenha espaços no início ou no fim
 * e que respeite um limite de tamanho.
 */
@Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
@Pattern(regexp = "^$|^\\S(?:.*\\S)?$", message = "A descrição não pode conter espaços no início ou no fim.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDescription {
    String message() default "Descrição inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}