package com.upgeekapi.mapper;

import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Interface gerenciada pelo MapStruct para conversões relacionadas à entidade {@link Product}.
 * <p>
 * Centraliza a lógica de mapeamento entre DTOs de requisição, a entidade de domínio
 * e os DTOs de resposta HATEOAS, garantindo uma arquitetura limpa e desacoplada.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    // --- Mapeamentos de Requisição (RequestDTO) para Entidade ---

    /**
     * Atualiza uma entidade Product existente a partir de um DTO de requisição.
     * A anotação @MappingTarget instrui o MapStruct a modificar a instância fornecida
     * em vez de criar uma nova.
     * <p>
     * A estratégia {@code NullValuePropertyMappingStrategy.IGNORE} é usada para
     * que campos nulos no DTO não sobrescrevam valores existentes na entidade,
     * permitindo atualizações parciais (estilo PATCH) de forma segura.
     *
     * @param dto O DTO com os dados de origem.
     * @param product A entidade de destino que será atualizada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true) // Boa prática: Ignora o ID para não tentar atualizá-lo.
    void updateProductFromDto(ProductRequestDTO dto, @MappingTarget Product product);

    /**
     * Mapeia um DTO de requisição para uma nova entidade Product.
     * @param dto O DTO com os dados de origem.
     * @return Uma nova entidade Product, pronta para ser salva.
     */
    @Mapping(target = "id", ignore = true) // Ignora o ID, pois é uma nova entidade.
    Product toEntity(ProductRequestDTO dto);

    /**
     * Método de ciclo de vida executado após o mapeamento de um {@link ProductRequestDTO} para um {@link Product}.
     * Garante que a lógica de negócio para o preço de desconto seja aplicada corretamente.
     */
    @AfterMapping
    default void handleDiscountPrice(@MappingTarget Product product) {
        if (!product.isOnSale()) {
            product.setDiscountPrice(null);
        }
    }

    // --- Mapeamento de Entidade para Resposta (HATEOAS DTO) ---

    /**
     * Converte a entidade {@link Product} para o seu modelo de representação HATEOAS {@link ProductHateoasDTO}.
     * <p>
     * Este é o mapeamento principal para as respostas da API. O MapStruct lida com a
     * cópia dos campos com nomes correspondentes automaticamente.
     *
     * @param product A entidade de domínio a ser convertida.
     * @return O DTO de resposta HATEOAS.
     */
    ProductHateoasDTO toHateoasDTO(Product product);

}