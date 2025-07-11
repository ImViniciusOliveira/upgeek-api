package com.upgeekapi.controller.assembler;

import com.upgeekapi.controller.ProductController;
import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.entity.RoleEnum;
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
 * Constrói o modelo de representação HATEOAS para a entidade {@link Product}.
 * <p>
 * Esta classe implementa o padrão "Assembler" do Spring HATEOAS, desacoplando a
 * lógica de construção de links da camada de Controller. Ela utiliza o {@link ProductMapper}
 * para a conversão de dados e, em seguida, enriquece o DTO com links contextuais,
 * incluindo links administrativos que são adicionados condicionalmente com base
 * nas permissões do usuário autenticado.
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
        super(ProductController.class, ProductHateoasDTO.class);
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converte uma entidade {@link Product} em um {@link ProductHateoasDTO} e adiciona
     * os links HATEOAS relevantes, que podem ser de três tipos:
     * <ul>
     *     <li><b>Links Estruturais:</b> Para o próprio recurso (self) e para a coleção (collection).</li>
     *     <li><b>Links de Descoberta:</b> Links para cada tag, permitindo a navegação para produtos relacionados.</li>
     *     <li><b>Links Condicionais:</b> Links de ações (update, delete) que aparecem apenas para usuários com permissão de administrador.</li>
     * </ul>
     */
    @Override
    @NonNull
    @SuppressWarnings("ConstantConditions") // Suprime avisos da IDE sobre o uso de 'null' em methodOn, que é o comportamento esperado pelo HATEOAS.
    public ProductHateoasDTO toModel(@NonNull Product entity) {
        // 1. Delega a conversão de dados para o MapStruct, mantendo a lógica centralizada.
        ProductHateoasDTO model = mapper.toHateoasDTO(entity);

        // 2. Enriquece o modelo com links HATEOAS estruturais e de descoberta.
        model.add(linkTo(methodOn(ProductController.class).getProductById(entity.getId())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("collection"));

        // Adiciona um link para cada tag do produto, permitindo a descoberta de produtos relacionados.
        Optional.ofNullable(entity.getTags())
                .ifPresent(tags -> tags.forEach(tagName ->
                        model.add(linkTo(methodOn(ProductController.class).getProductsByTag(tagName))
                                .withRel("tag").withTitle(tagName))
                ));

        // 3. Adiciona links de ações administrativas condicionalmente.
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
     * Verifica de forma segura se o usuário atualmente autenticado possui o papel de 'ADMIN'.
     * <p>
     * Este método auxiliar encapsula a lógica de verificação de permissão, garantindo que
     * a checagem seja robusta e não lance exceções para usuários anônimos ou não autenticados,
     * o que é comum em endpoints públicos. O uso do enum {@link RoleEnum} torna a verificação
     * "type-safe", prevenindo erros de digitação.
     *
     * @return {@code true} se o usuário for um administrador, {@code false} caso contrário.
     */
    private boolean checkUserHasAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Defensiva: Garante que a verificação não falhe para usuários anônimos ou não autenticados.
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        // A verificação usa o enum, tornando-a type-safe e mais robusta.
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(RoleEnum.ROLE_ADMIN.getAuthority()));
    }
}