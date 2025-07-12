package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ValidDescription;
import com.upgeekapi.validation.annotation.ValidDiscount;
import com.upgeekapi.validation.annotation.ValidImageUrl;
import com.upgeekapi.validation.annotation.ValidProductName;
import com.upgeekapi.validation.annotation.ValidTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

@Schema(description = "Dados necessários para criar ou atualizar um produto.")
@ValidDiscount
public record ProductRequestDTO(

        @ValidProductName
        @Schema(description = "O nome do produto.", example = "Estátua de Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @ValidDescription
        @Schema(description = "A descrição detalhada do produto (opcional).", example = "Estátua de resina de alta qualidade...")
        String description,

        @NotNull(message = "O preço original é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        @Schema(description = "O preço original do produto.", example = "599.90", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal originalPrice,

        @PositiveOrZero(message = "O preço com desconto não pode ser negativo.")
        @Schema(description = "O preço com desconto. Obrigatório se 'onSale' for true.", example = "499.90")
        BigDecimal discountPrice,

        @NotNull(message = "É obrigatório indicar se o produto está em promoção.")
        @Schema(description = "Indica se o produto está atualmente em promoção.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean onSale,

        @NotNull(message = "A quantidade de XP é obrigatória.")
        @PositiveOrZero(message = "O XP não pode ser negativo.")
        @Schema(description = "A quantidade de XP concedida ao adquirir o produto.", example = "2500", requiredMode = Schema.RequiredMode.REQUIRED)
        Long xp,

        @ValidImageUrl
        @Schema(description = "A URL da imagem principal do produto.", example = "/assets/images/lira-valen.webp", requiredMode = Schema.RequiredMode.REQUIRED)
        String imageUrl,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        @Schema(description = "A quantidade de itens em estoque.", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer stockQuantity,

        @Schema(description = "Conjunto de tags ou categorias associadas ao produto.", example = "[\"alianca-scarlate\", \"edicao-limitada\"]")
        Set<@ValidTag String> tags
) {}