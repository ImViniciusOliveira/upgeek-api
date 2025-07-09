package com.upgeekapi.validation;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.validation.annotation.ValidDiscount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ValidDiscountValidator implements ConstraintValidator<ValidDiscount, ProductRequestDTO> {

    @Override
    public boolean isValid(ProductRequestDTO product, ConstraintValidatorContext context) {
        if (product == null) {
            return true; // Não validar se o objeto for nulo
        }

        Boolean onSale = product.onSale();
        BigDecimal discountPrice = product.discountPrice();
        BigDecimal originalPrice = product.originalPrice();

        // Se 'onSale' não for informado, a validação @NotNull no campo já vai pegar.
        if (onSale == null) {
            return true;
        }

        if (onSale) {
            // Se está em promoção, o preço com desconto é obrigatório e deve ser menor que o original.
            if (discountPrice == null) {
                // Adiciona a mensagem de erro especificamente ao campo 'discountPrice'
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O preço com desconto é obrigatório quando o produto está em promoção.")
                        .addPropertyNode("discountPrice")
                        .addConstraintViolation();
                return false;
            }
            if (originalPrice != null && discountPrice.compareTo(originalPrice) >= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O preço com desconto deve ser menor que o preço original.")
                        .addPropertyNode("discountPrice")
                        .addConstraintViolation();
                return false;
            }
        } else {
            // Se NÃO está em promoção, o preço com desconto DEVE ser nulo.
            if (discountPrice != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O preço com desconto deve ser nulo quando o produto não está em promoção.")
                        .addPropertyNode("discountPrice")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}