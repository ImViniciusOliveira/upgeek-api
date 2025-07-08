package com.upgeekapi.dto.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Modelo de representação HATEOAS para um Produto.
 * Estende RepresentationModel para poder carregar links.
 */
@Data
@EqualsAndHashCode(callSuper = false) // Essencial para evitar problemas de igualdade com a classe pai
@JsonRootName(value = "product") // Define um nome raiz para o objeto no JSON (opcional, mas bom estilo)
@Relation(collectionRelation = "products", itemRelation = "product") // Define os nomes para coleções e itens no JSON HATEOAS
@JsonInclude(JsonInclude.Include.NON_NULL) // Otimização: não inclui campos nulos no JSON final
public class ProductHateoasDTO extends RepresentationModel<ProductHateoasDTO> {
    private Long id;
    private String name;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private boolean onSale;
    private Integer xp; // Corrigido para Integer para corresponder à entidade
    private String imageUrl;
    private Integer stockQuantity; // Corrigido para Integer para corresponder à entidade
    private Set<String> tags;
}