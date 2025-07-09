package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ValidDiscount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO que representa os dados para criar ou atualizar um produto.
 */
@Schema(description = "Dados necessários para criar ou atualizar um produto.")
@ValidDiscount
public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Schema(description = "O nome do produto.", example = "Estátua de Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "A descrição detalhada do produto.")
        String description,

        @NotNull(message = "O preço original é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        @Schema(description = "O preço original do produto.", example = "599.90", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal originalPrice,

        @PositiveOrZero(message = "O preço com desconto não pode ser negativo.")
        @Schema(description = "O preço com desconto. Deve ser informado se 'onSale' for true. Pode ser nulo se 'onSale' for false.", example = "499.90")
        BigDecimal discountPrice,

        @NotNull(message = "É obrigatório indicar se o produto está em promoção.")
        @Schema(description = "Indica se o produto está atualmente em promoção. Controla a relevância do campo 'discountPrice'.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean onSale,

        @NotNull(message = "A quantidade de XP é obrigatória.")
        @PositiveOrZero(message = "O XP não pode ser negativo.")
        @Schema(description = "A quantidade de XP concedida ao adquirir o produto.", example = "2500", requiredMode = Schema.RequiredMode.REQUIRED)
        Long xp,

        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Schema(description = "A URL da imagem principal do produto.", example = "/assets/images/lira-valen.webp", requiredMode = Schema.RequiredMode.REQUIRED)
        String imageUrl,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        @Schema(description = "A quantidade de itens em estoque.", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer stockQuantity,

        @Schema(description = "Conjunto de tags ou categorias associadas ao produto.", example = "[\"alianca-scarlate\", \"edicao-limitada\"]")
        Set<String> tags
) {}