package com.upgeekapi.mapper;

import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface gerenciada pelo MapStruct para converter a entidade {@link Product} em seu DTO de resposta.
 * <p>
 * Seguindo os princípios de um modelo de domínio rico, a lógica de criação e atualização
 * reside na própria entidade {@link Product}. Este mapper, portanto, tem a responsabilidade
 * única e focada de mapear a entidade para sua representação HATEOAS, {@link ProductHateoasDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    // A responsabilidade de mapear de DTO de requisição para Entidade foi movida
    // para a própria entidade Product (métodos 'from' e 'updateFrom')
    // para um melhor encapsulamento e para centralizar as regras de negócio.

    /**
     * Converte uma única entidade {@link Product} para o seu DTO de resposta HATEOAS.
     * <p>
     * O MapStruct lida com a cópia dos campos com nomes correspondentes automaticamente.
     *
     * @param product A entidade de domínio a ser convertida.
     * @return O DTO de resposta HATEOAS, pronto para receber os links de ações.
     */
    // Ignora o campo 'links' do RepresentationModel, que será preenchido pelo Assembler.
    @Mapping(target = "links", ignore = true)
    ProductHateoasDTO toHateoasDTO(Product product);

    /**
     * Converte uma lista de entidades {@link Product} para uma lista de DTOs de resposta HATEOAS.
     * <p>
     * O MapStruct aplicará automaticamente a conversão definida em {@link #toHateoasDTO(Product)}
     * para cada elemento da lista.
     *
     * @param products A lista de entidades de domínio a serem convertidas.
     * @return Uma lista de DTOs de resposta HATEOAS.
     */
    List<ProductHateoasDTO> toHateoasDTO(List<Product> products);

}