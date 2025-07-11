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
 * Centraliza a lógica de geração de links HATEOAS para a raiz da API,
 * adaptando os links disponíveis com base no estado de autenticação do usuário.
 */
@Service
public class ApiRootLinkServiceImpl implements ApiRootLinkService {

    /**
     * {@inheritDoc}
     * <p>
     * Constrói o modelo de representação com links dinâmicos. A supressão de warning
     * "ConstantConditions" é necessária porque o Spring HATEOAS utiliza proxies que
     * podem ser interpretados incorretamente como nulos pela análise estática.
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public RepresentationModel<?> buildApiRootLinks(Authentication authentication) {
        RepresentationModel<?> rootModel = new RepresentationModel<>();

        // Adiciona links públicos, que estão sempre disponíveis para qualquer usuário.
        rootModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));

        // Adiciona links contextuais, disponíveis apenas para usuários autenticados.
        if (isUserAuthenticated(authentication)) {
            // O argumento 'null' é um placeholder para satisfazer a assinatura do método
            // no controller, permitindo a geração correta do link HATEOAS.
            rootModel.add(linkTo(methodOn(AccountController.class).getAccount(null)).withRel("account"));
            rootModel.add(linkTo(methodOn(AuthController.class).getAuthenticatedUser(null)).withRel("authentication"));
        }

        // Adiciona o link para o próprio recurso (self-referencing), uma prática padrão em HATEOAS.
        rootModel.add(linkTo(methodOn(ApiRootController.class).getApiRoot()).withSelfRel());

        return rootModel;
    }

    /**
     * Verifica se o objeto de autenticação representa um usuário genuinamente logado.
     * <p>
     * Este método é crucial para diferenciar um usuário autenticado de um acesso anônimo,
     * que o Spring Security representa através de um {@link AnonymousAuthenticationToken}.
     *
     * @param authentication O objeto de autenticação do contexto de segurança.
     * @return {@code true} se o usuário estiver autenticado e não for anônimo, {@code false} caso contrário.
     */
    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }
}