package com.upgeekapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * DTO que representa os dados públicos de um produto para exibição na API.
 */
@Schema(description = "Dados públicos de um produto para exibição.")
public record ProductDTO(
        @Schema(description = "O ID único do produto.", example = "1")
        Long id,

        @Schema(description = "O nome do produto.", example = "Estátua de Lira Valen")
        String name,

        @Schema(description = "A descrição detalhada do produto.")
        String description,

        @Schema(description = "O preço original do produto (o 'de').")
        BigDecimal originalPrice,

        @Schema(description = "O preço com desconto, se aplicável (o 'por').")
        BigDecimal discountPrice,

        @Schema(description = "Indica se o produto está atualmente em promoção.")
        boolean onSale,

        @Schema(description = "A quantidade de XP concedida ao adquirir o produto.", example = "2500")
        Long xp,

        @Schema(description = "A URL da imagem principal do produto.", example = "/assets/images/lira-valen.webp")
        String imageUrl
) {}