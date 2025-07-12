package com.upgeekapi.validation.validator;

import com.upgeekapi.dto.request.ProductSearchRequestDto;
import com.upgeekapi.validation.annotation.ValidPriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Implementa a lógica de validação para a anotação {@link ValidPriceRange}.
 */
public class ValidPriceRangeValidator implements ConstraintValidator<ValidPriceRange, ProductSearchRequestDto> {

    @Override
    public boolean isValid(ProductSearchRequestDto dto, ConstraintValidatorContext context) {
        BigDecimal minPrice = dto.minPrice();
        BigDecimal maxPrice = dto.maxPrice();

        // Se um dos valores (ou ambos) for nulo, a validação não se aplica,
        // pois a busca pode ser aberta em um dos lados.
        if (minPrice == null || maxPrice == null) {
            return true;
        }

        // Se ambos os valores estiverem presentes, o mínimo não pode ser maior que o máximo.
        // compareTo retorna > 0 se minPrice for maior que maxPrice.
        return minPrice.compareTo(maxPrice) <= 0;
    }
}