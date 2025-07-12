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
 * Anotação de composição para validar uma tag de produto.
 * Garante que a tag não seja vazia, respeite o limite de tamanho e
 * siga uma whitelist estrita de caracteres.
 */
@NotBlank(message = "A tag não pode ser vazia.")
@Size(max = 50, message = "A tag não pode exceder 50 caracteres.")
@Pattern(regexp = "^[a-z0-9-]+$", message = "A tag pode conter apenas letras minúsculas, números e hífens.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTag {
    String message() default "Tag inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}