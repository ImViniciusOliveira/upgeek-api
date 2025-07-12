package com.upgeekapi.dto.request;

import com.upgeekapi.validation.annotation.ValidPriceRange;
import com.upgeekapi.validation.annotation.ValidSearchTerm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Critérios de busca para filtrar produtos.")
@ValidPriceRange
public record ProductSearchRequestDto(

        @ValidSearchTerm
        @Schema(description = "Busca por produtos que contenham este texto no nome.", example = "Kain")
        String name,

        @PositiveOrZero(message = "O preço mínimo não pode ser negativo.")
        @Schema(description = "Filtra produtos com preço maior ou igual a este valor.", example = "100.00")
        BigDecimal minPrice,

        @PositiveOrZero(message = "O preço máximo não pode ser negativo.")
        @Schema(description = "Filtra produtos com preço menor ou igual a este valor.", example = "1500.00")
        BigDecimal maxPrice
) {}