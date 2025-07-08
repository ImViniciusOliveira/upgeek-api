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
 * Converte a entidade {@link Product} em seu DTO HATEOAS {@link ProductHateoasDTO},
 * adicionando links de navegação e ações condicionais baseadas na permissão do usuário.
 */
@Component
public class ProductHateoasAssembler extends RepresentationModelAssemblerSupport<Product, ProductHateoasDTO> {

    private final ProductMapper mapper;

    public ProductHateoasAssembler(ProductMapper mapper) {
        super(ProductController.class, ProductHateoasDTO.class);
        this.mapper = mapper;
    }

    @Override
    public ProductHateoasDTO toModel(@NonNull Product entity) {
        ProductHateoasDTO model = mapper.toHateoasDTO(entity);

        // Links de navegação padrão
        model.add(linkTo(methodOn(ProductController.class).getProductById(entity.getId())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("collection"));

        // Links para cada tag do produto
        Optional.ofNullable(entity.getTags())
                .ifPresent(tags -> tags.forEach(tagName ->
                        model.add(linkTo(methodOn(ProductController.class).getProductsByTag(tagName))
                                .withRel("tag").withTitle(tagName))
                ));

        // Adiciona links de AÇÃO (update/delete) apenas se o usuário for um administrador.
        // O frontend pode usar a presença desses links para decidir se renderiza os botões de edição.
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
     * Verifica se o usuário autenticado possui a permissão 'ROLE_ADMIN' usando o contexto
     * de segurança do Spring.
     *
     * @return {@code true} se o usuário for um admin, {@code false} caso contrário.
     */
    private boolean checkUserHasAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }

        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}