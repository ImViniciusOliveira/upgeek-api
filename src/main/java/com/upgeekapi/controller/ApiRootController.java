package com.upgeekapi.controller;

import com.upgeekapi.service.ApiRootLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller que expõe o ponto de entrada raiz da API (API Root).
 * <p>
 * Seguindo os princípios HATEOAS, este endpoint serve como o ponto de partida
 * para a descoberta de outros recursos. Ele delega a construção dos links
 * para o {@link ApiRootLinkService}, que adapta a resposta com base no
 * estado de autenticação do usuário.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "API Root", description = "Ponto de entrada da API")
public class ApiRootController {

    private final ApiRootLinkService linkService;

    /**
     * Retorna os links para os principais recursos da API, permitindo a descoberta.
     * <p>
     * A resposta é dinâmica: usuários não autenticados verão links para login e registro,
     * enquanto usuários autenticados verão links para os recursos de sua conta.
     *
     * @param authentication O objeto de autenticação do usuário, injetado automaticamente
     *                       pelo Spring Security. Será um {@code AnonymousAuthenticationToken}
     *                       para usuários não autenticados.
     * @return Um modelo de representação HATEOAS com os links de navegação disponíveis.
     */
    @GetMapping
    @Operation(summary = "Descobrir os recursos disponíveis da API",
            description = "Retorna os links para os recursos que o usuário atual pode acessar, seguindo os princípios HATEOAS.")
    public RepresentationModel<?> getApiRoot(Authentication authentication) {
        // Em vez de buscar manualmente no SecurityContextHolder, recebemos a autenticação
        // como um parâmetro. Isso torna o controller mais limpo, declarativo e fácil de testar.
        return linkService.buildApiRootLinks(authentication);
    }
}