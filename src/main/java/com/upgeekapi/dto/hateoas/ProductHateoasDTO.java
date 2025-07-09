package com.upgeekapi.dto.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Modelo de representação HATEOAS para um Produto.
 * <p>
 * Este DTO é projetado para ser largamente imutável, com dados definidos
 * via construtor. Ele contém todos os dados de um produto e estende {@link RepresentationModel}
 * para poder carregar links de ações da API.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "product")
@Relation(collectionRelation = "products", itemRelation = "product")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ProductResponse", description = "Representação de um produto com links HATEOAS para ações disponíveis.")
public class ProductHateoasDTO extends RepresentationModel<ProductHateoasDTO> {

    @Schema(description = "Identificador único do produto.", example = "101")
    private Long id;

    @Schema(description = "Nome do produto.", example = "Action Figure do Batman")
    private String name;

    @Schema(description = "Descrição detalhada do produto.", example = "Action figure detalhada do Batman, da linha DC Multiverse, com 22 pontos de articulação.")
    private String description;

    @Schema(description = "Preço original do produto.", example = "299.90")
    private BigDecimal originalPrice;

    @Schema(description = "Preço com desconto, se o produto estiver em promoção. Será nulo se onSale for false.", example = "249.90")
    private BigDecimal discountPrice;

    @Schema(description = "Indica se o produto está atualmente em promoção.", example = "true")
    private boolean onSale;

    @Schema(description = "Pontos de experiência (XP) que o usuário ganha ao adquirir este produto.", example = "300")
    private Integer xp;

    @Schema(description = "URL da imagem principal do produto.", example = "https://example.com/images/batman-figure.jpg")
    private String imageUrl;

    @Schema(description = "Quantidade do produto disponível em estoque.", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Conjunto de tags associadas ao produto para categorização.", example = "[\"DC Comics\", \"Batman\", \"Action Figure\"]")
    private Set<String> tags;
}