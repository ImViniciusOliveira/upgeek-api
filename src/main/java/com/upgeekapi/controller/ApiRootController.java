package com.upgeekapi.controller;

import com.upgeekapi.service.ApiRootLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller que expõe o ponto de entrada raiz da API.
 * Delega a construção dos links para um serviço dedicado.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "API Root", description = "Ponto de entrada da API")
public class ApiRootController {

    private final ApiRootLinkService linkService;

    @GetMapping
    @Operation(summary = "Descobrir os recursos disponíveis da API",
            description = "Retorna os links para os recursos que o usuário atual pode acessar.")
    public RepresentationModel<?> getApiRoot() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return linkService.buildApiRootLinks(authentication);
    }
}