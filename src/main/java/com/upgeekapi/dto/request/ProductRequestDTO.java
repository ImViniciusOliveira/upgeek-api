package com.upgeekapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO que representa os dados para criar ou atualizar um produto.
 */
@Schema(description = "Dados necessários para criar ou atualizar um produto.")
public record ProductRequestDTO(
        @Schema(description = "O nome do produto.", example = "Estátua de Lira Valen", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do produto é obrigatório.")
        String name,

        @Schema(description = "A descrição detalhada do produto.")
        String description,

        @Schema(description = "O preço original do produto.", example = "599.90", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O preço original é obrigatório.")
        @Min(value = 0, message = "O preço não pode ser negativo.")
        BigDecimal originalPrice,

        @Schema(description = "O preço com desconto, se o produto estiver em promoção. Caso contrário, pode ser nulo.", example = "499.90")
        @Min(value = 0, message = "O preço com desconto não pode ser negativo.")
        BigDecimal discountPrice,

        @Schema(description = "Indica se o produto está atualmente em promoção.", example = "true")
        Boolean onSale,

        @Schema(description = "A quantidade de XP concedida ao adquirir o produto.", example = "2500", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A quantidade de XP é obrigatória.")
        @Min(value = 0, message = "O XP não pode ser negativo.")
        Long xp,

        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Schema(description = "A URL da imagem principal do produto.", example = "/assets/images/lira-valen.webp", requiredMode = Schema.RequiredMode.REQUIRED)
        String imageUrl,

        @Schema(description = "A quantidade de itens em estoque.", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @Min(value = 0, message = "O estoque não pode ser negativo.")
        Integer stockQuantity
) {}