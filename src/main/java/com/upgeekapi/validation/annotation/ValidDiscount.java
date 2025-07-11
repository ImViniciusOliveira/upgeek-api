package com.upgeekapi.validation.annotation;

import com.upgeekapi.validation.ValidDiscountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de validação customizada para garantir a consistência da lógica de desconto em um produto.
 * <p>
 * Esta anotação deve ser aplicada no nível da classe ({@code ElementType.TYPE}) de um DTO
 * que contenha os campos de promoção. Ela valida as seguintes regras de negócio:
 * <ol>
 *     <li>Se um produto está em promoção ({@code onSale = true}), seu {@code discountPrice} deve ser informado e menor que o {@code originalPrice}.</li>
 *     <li>Se um produto <b>não</b> está em promoção ({@code onSale = false}), seu {@code discountPrice} deve ser nulo.</li>
 * </ol>
 *
 * @see ValidDiscountValidator
 */
@Constraint(validatedBy = ValidDiscountValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscount {
    String message() default "A lógica de preço e promoção é inválida.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}