package com.upgeekapi.controller.assembler;

import com.upgeekapi.controller.ProductController;
import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.mapper.ProductMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Montador (Assembler) responsável por converter a entidade {@link Product}
 * em seu modelo de representação HATEOAS, o {@link ProductHateoasDTO}.
 * <p>
 * Esta classe desacopla a lógica de construção de links da camada de Controller.
 * Ela primeiro usa o {@link ProductMapper} para a conversão dos dados e, em seguida,
 * enriquece o DTO com links contextuais que representam as ações possíveis na API.
 */
@Component
public class ProductHateoasAssembler extends RepresentationModelAssemblerSupport<Product, ProductHateoasDTO> {

    private final ProductMapper mapper;

    /**
     * Construtor que injeta as dependências necessárias e configura a classe base do HATEOAS.
     *
     * @param mapper O mapper para converter a entidade Product para seu DTO de dados.
     */
    public ProductHateoasAssembler(ProductMapper mapper) {
        // Configura a classe base com o Controller e o DTO de destino.
        super(ProductController.class, ProductHateoasDTO.class);
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converte uma entidade {@link Product} em um {@link ProductHateoasDTO} e adiciona
     * os links HATEOAS relevantes. Links administrativos como 'update' e 'delete'
     * são adicionados condicionalmente, com base no papel do usuário autenticado.
     */
    @Override
    public ProductHateoasDTO toModel(@NonNull Product entity) {
        // 1. Delega a conversão de dados para o MapStruct, mantendo a lógica centralizada.
        ProductHateoasDTO model = mapper.toHateoasDTO(entity);

        // 2. Enriquece o modelo com links HATEOAS.
        // Link para o próprio recurso (self-referencing).
        model.add(linkTo(methodOn(ProductController.class).getProductById(entity.getId())).withSelfRel());
        // Link para a coleção de todos os produtos.
        model.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("collection"));

        // Adiciona um link para cada tag do produto, permitindo a descoberta de produtos relacionados.
        Optional.ofNullable(entity.getTags())
                .ifPresent(tags -> tags.forEach(tagName ->
                        model.add(linkTo(methodOn(ProductController.class).getProductsByTag(tagName))
                                .withRel("tag").withTitle(tagName))
                ));

        // 3. Adiciona links condicionalmente, apenas para administradores.
        if (checkUserHasAdminRole()) {
            model.add(linkTo(methodOn(ProductController.class)
                    .updateProduct(entity.getId(), null))
                    .withRel("update"));

            model.add(linkTo(methodOn(ProductController.class)
                    .deleteProduct(entity.getId()))
                    .withRel("delete"));
        }

        return model;
    }

    /**
     * Verifica de forma segura se o usuário atualmente autenticado possui o papel de 'ROLE_ADMIN'.
     * <p>
     * Este método auxiliar encapsula a lógica de segurança para determinar se
     * os links de ações administrativas devem ser adicionados ao modelo de resposta.
     *
     * @return {@code true} se o usuário for um administrador, {@code false} caso contrário.
     */
    private boolean checkUserHasAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Defensiva: Garante que a verificação não falhe para usuários anônimos ou não autenticados.
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}