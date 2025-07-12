package com.upgeekapi.validation.annotation;

import com.upgeekapi.validation.validator.ValidPriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de validação a nível de classe para garantir que, se ambos os preços
 * mínimo e máximo forem fornecidos, o mínimo não seja maior que o máximo.
 */
@Constraint(validatedBy = ValidPriceRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriceRange {
    String message() default "O preço mínimo não pode ser maior que o preço máximo.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}