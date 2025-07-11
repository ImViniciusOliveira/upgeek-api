package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO que representa os dados públicos de um produto para exibição na API.
 */
@Schema(description = "Dados públicos de um produto para exibição.")
public record ProductDTO(
        @Schema(description = "O ID único do produto.", example = "1")
        Long id,

        @Schema(description = "O nome do produto.", example = "Armadura de Renegado de Kain")
        String name,

        @Schema(description = "A descrição detalhada do produto.", example = "Tecnologia híbrida, um símbolo de rebelião e poder.")
        String description,

        @Schema(description = "O preço original do produto (o 'de').", example = "1499.90")
        BigDecimal originalPrice,

        @Schema(description = "O preço com desconto, se aplicável (o 'por').", example = "1299.90")
        BigDecimal discountPrice,

        @Schema(description = "Indica se o produto está atualmente em promoção.")
        boolean onSale,

        @Schema(description = "A quantidade de XP concedida ao adquirir o produto.", example = "5000")
        Long xp,

        @Schema(description = "A URL da imagem principal do produto.", example = "/assets/images/kain-armor.webp")
        String imageUrl,

        @Schema(description = "As tags ou categorias do produto.", example = "[\"renegado\", \"tecnologia-stealth\"]")
        Set<String> tags
) {}