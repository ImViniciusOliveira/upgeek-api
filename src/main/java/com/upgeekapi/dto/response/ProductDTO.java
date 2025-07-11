package com.upgeekapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO que representa os dados públicos de um produto para exibição na API.
 * <p>
 * Este record imutável é projetado para ser uma representação "plana" (sem HATEOAS)
 * e focada nos dados de um produto, omitindo informações de gerenciamento como
 * quantidade em estoque. É ideal para listagens ou contextos onde apenas os dados
 * essenciais são necessários.
 *
 * @param id O ID único do produto.
 * @param name O nome do produto.
 * @param description A descrição detalhada do produto.
 * @param originalPrice O preço original do produto (o 'de').
 * @param discountPrice O preço com desconto, se aplicável (o 'por'). Será nulo se o produto não estiver em promoção.
 * @param onSale Indica se o produto está atualmente em promoção.
 * @param xp A quantidade de XP concedida ao adquirir o produto.
 * @param imageUrl A URL da imagem principal do produto.
 * @param tags As tags ou categorias do produto.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ProductData", description = "Dados públicos de um produto para exibição.")
public record ProductDTO(
        @Schema(description = "O ID único do produto.", example = "1")
        Long id,

        @Schema(description = "O nome do produto.", example = "Armadura de Renegado de Kain")
        String name,

        @Schema(description = "A descrição detalhada do produto.", example = "Tecnologia híbrida, um símbolo de rebelião e poder.")
        String description,

        @Schema(description = "O preço original do produto (o 'de').", example = "1499.90")
        BigDecimal originalPrice,

        @Schema(description = "O preço com desconto, se aplicável (o 'por'). Será nulo se o produto não estiver em promoção.", example = "1299.90")
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