package com.upgeekapi.validation.annotation;

import com.upgeekapi.validation.ValidDiscountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidDiscountValidator.class)
@Target({ ElementType.TYPE }) // Esta anotação será usada no nível da classe
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscount {
    String message() default "A lógica de desconto é inválida. Se 'onSale' for true, o preço com desconto é obrigatório. Se for false, o preço com desconto deve ser nulo.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}