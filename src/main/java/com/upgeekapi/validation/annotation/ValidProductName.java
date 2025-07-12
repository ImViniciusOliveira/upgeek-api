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
 * Anotação de composição para validar um nome de produto.
 * Agrupa as regras de não-nulidade, tamanho e a whitelist de caracteres
 * permitidos (letras, números, espaços e alguns símbolos).
 */
@NotBlank(message = "O nome do produto é obrigatório.")
@Size(max = 255, message = "O nome do produto não pode exceder 255 caracteres.")
@Pattern(
        regexp = "^[a-zA-Z0-9áéíóúâêîôûãõçÁÉÍÓÚÂÊÎÔÛÃÕÇ' -]+$",
        message = "O nome do produto contém caracteres inválidos. Apenas letras, números, espaços, ' e - são permitidos."
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "Nome de produto inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}