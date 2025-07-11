package com.upgeekapi.service.impl;

import com.upgeekapi.controller.AccountController;
import com.upgeekapi.controller.ApiRootController;
import com.upgeekapi.controller.AuthController;
import com.upgeekapi.controller.ProductController;
import com.upgeekapi.service.ApiRootLinkService;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementação do serviço {@link ApiRootLinkService}.
 * <p>
 * Esta classe é responsável por construir o ponto de entrada (API Root) da aplicação,
 * seguindo os princípios HATEOAS. Ela gera uma resposta que contém links para os
 * principais recursos da API, adaptando-os dinamicamente com base no estado de
 * autenticação do usuário.
 */
@Service
public class ApiRootLinkServiceImpl implements ApiRootLinkService {

    /**
     * {@inheritDoc}
     * <p>
     * Constrói o modelo de representação com links dinâmicos. Links públicos são sempre
     * incluídos, enquanto links para recursos protegidos (como a conta do usuário)
     * são adicionados apenas se o usuário estiver autenticado.
     * <p>
     * <strong>Nota sobre {@code ConstantConditions}:</strong> A supressão deste warning
     * é necessária porque o Spring HATEOAS utiliza proxies para construir os links,
     * o que pode ser interpretado incorretamente como uma condição nula pela análise estática do código.
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public RepresentationModel<?> buildApiRootLinks(Authentication authentication) {
        RepresentationModel<?> rootModel = new RepresentationModel<>();

        // 1. Adiciona links de recursos públicos, sempre disponíveis.
        rootModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));

        // 2. Adiciona links contextuais baseados no estado de autenticação.
        if (isUserAuthenticated(authentication)) {
            // Para usuários autenticados, oferece links para gerenciamento de conta.
            // O 'null' é um placeholder para satisfazer a assinatura do método no controller.
            rootModel.add(linkTo(methodOn(AccountController.class).getAccount(null)).withRel("account"));
            rootModel.add(linkTo(methodOn(AuthController.class).getAuthenticatedUser(null)).withRel("me"));
        } else {
            // Para usuários não autenticados, oferece os links para login e registro.
            rootModel.add(linkTo(methodOn(AuthController.class).login(null)).withRel("login"));
            rootModel.add(linkTo(methodOn(AuthController.class).register(null)).withRel("register"));
        }

        // 3. Adiciona o link para o próprio recurso (self-referencing), essencial em HATEOAS.
        rootModel.add(linkTo(methodOn(ApiRootController.class).getApiRoot(null)).withSelfRel());

        return rootModel;
    }

    /**
     * Verifica se o objeto de autenticação representa um usuário genuinamente logado.
     * <p>
     * Este método é crucial para diferenciar um usuário autenticado de um acesso anônimo,
     * que o Spring Security representa através de um {@link AnonymousAuthenticationToken}.
     *
     * @param authentication O objeto de autenticação do contexto de segurança, que pode ser nulo.
     * @return {@code true} se o usuário estiver autenticado e não for anônimo, {@code false} caso contrário.
     */
    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }
}