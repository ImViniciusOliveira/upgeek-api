package com.upgeekapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller que expõe o ponto de entrada raiz da API.
 * <p>
 * Este endpoint serve como um "diretório" dinâmico, exibindo links para os
 * recursos que o usuário atual tem permissão para acessar.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "API Root", description = "Ponto de entrada da API")
public class ApiRootController {

    @GetMapping
    @Operation(summary = "Descobrir os recursos disponíveis da API",
            description = "Retorna os links para os recursos que o usuário atual pode acessar.")
    @SuppressWarnings("ConstantConditions")
    public RepresentationModel<?> getApiRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Links que são sempre públicos
        rootModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));

        // Links que aparecem apenas para usuários autenticados
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            rootModel.add(linkTo(methodOn(AccountController.class).getAccount(null)).withRel("account"));
            rootModel.add(linkTo(methodOn(AuthController.class).getAuthenticatedUser(null)).withRel("authentication"));
        }

        // Adiciona o link para o próprio recurso
        rootModel.add(linkTo(methodOn(ApiRootController.class).getApiRoot()).withSelfRel());

        return rootModel;
    }
}