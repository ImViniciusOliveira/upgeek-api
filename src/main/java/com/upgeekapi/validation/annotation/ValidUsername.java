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
 * Anotação de composição para validar o formato de um nome de usuário.
 * <p>
 * Garante que, se o nome de usuário for fornecido, ele respeite as regras de
 * tamanho e a whitelist de caracteres (alfanuméricos e espaços internos).
 * Esta anotação não valida a presença, sendo ideal para campos opcionais.
 */
@Size(min = 3, max = 16, message = "O nome de usuário deve ter entre 3 e 16 caracteres.")
@Pattern(regexp = "^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*$", message = "O nome de usuário pode conter apenas letras, números e espaços internos.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Nome de usuário inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}