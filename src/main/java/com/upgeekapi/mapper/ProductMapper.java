package com.upgeekapi.mapper;

import com.upgeekapi.dto.response.ProductDTO;
import com.upgeekapi.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface gerenciada pelo MapStruct para converter a entidade {@link Product}
 * em um {@link ProductDTO}.
 * <p>
 * A implementação desta interface é gerada automaticamente em tempo de compilação.
 * Como todos os campos entre a entidade e o DTO possuem nomes correspondentes,
 * nenhuma configuração de mapeamento customizada é necessária.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Mapeia uma entidade Product para um ProductDTO.
     *
     * @param product A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    ProductDTO toDto(Product product);

    /**
     * Mapeia uma lista de entidades Product para uma lista de ProductDTOs.
     * O MapStruct gera o loop automaticamente.
     *
     * @param products A lista de entidades a ser convertida.
     * @return A lista de DTOs correspondente.
     */
    List<ProductDTO> toDto(List<Product> products);
}