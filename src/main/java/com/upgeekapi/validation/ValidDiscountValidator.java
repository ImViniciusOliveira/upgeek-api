package com.upgeekapi.validation;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.validation.annotation.ValidDiscount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Implementação da lógica de validação para a anotação {@link ValidDiscount}.
 * <p>
 * Valida as regras de negócio que governam a relação entre os campos de preço
 * e o status de promoção de um produto.
 */
public class ValidDiscountValidator implements ConstraintValidator<ValidDiscount, ProductRequestDTO> {

    private static final String DISCOUNT_PRICE_FIELD = "discountPrice";

    /**
     * Valida o DTO do produto contra as regras de desconto.
     *
     * @param product O objeto DTO a ser validado.
     * @param context O contexto no qual a validação ocorre, usado para reportar erros.
     * @return {@code true} se o DTO for válido, {@code false} caso contrário.
     */
    @Override
    public boolean isValid(ProductRequestDTO product, ConstraintValidatorContext context) {
        // Se o objeto for nulo, a validação não se aplica aqui.
        if (product == null) {
            return true;
        }

        Boolean onSale = product.onSale();
        BigDecimal discountPrice = product.discountPrice();
        BigDecimal originalPrice = product.originalPrice();

        // Se 'onSale' não for informado, a validação @NotNull no campo já vai tratar disso.
        if (onSale == null) {
            return true;
        }

        boolean isValid = true;

        if (onSale) {
            // Regra 1: Se está em promoção, o preço com desconto é obrigatório.
            if (discountPrice == null) {
                addConstraintViolation(context, "O preço com desconto é obrigatório quando o produto está em promoção.");
                isValid = false;
            }
            // Regra 2: O preço com desconto deve ser menor que o original.
            // Esta verificação só faz sentido se ambos os preços não forem nulos.
            else if (originalPrice != null && discountPrice.compareTo(originalPrice) >= 0) {
                addConstraintViolation(context, "O preço com desconto deve ser menor que o preço original.");
                isValid = false;
            }
        } else {
            // Regra 3: Se NÃO está em promoção, o preço com desconto DEVE ser nulo.
            if (discountPrice != null) {
                addConstraintViolation(context, "O preço com desconto deve ser nulo quando o produto não está em promoção.");
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Método auxiliar para construir e adicionar uma violação de constraint ao campo 'discountPrice'.
     *
     * @param context O contexto da validação.
     * @param message A mensagem de erro a ser exibida.
     */
    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(DISCOUNT_PRICE_FIELD)
                .addConstraintViolation();
    }
}