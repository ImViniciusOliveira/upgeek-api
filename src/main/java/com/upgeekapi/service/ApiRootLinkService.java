package com.upgeekapi.service;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;

/**
 * Contrato para o serviço responsável por construir o modelo de links
 * para o ponto de entrada da API (API Root).
 */
public interface ApiRootLinkService {

    /**
     * Constrói e retorna o modelo de representação para a raiz da API com base
     * no estado de autenticação do usuário.
     *
     * @param authentication O objeto de autenticação do usuário atual.
     * @return Um RepresentationModel contendo os links HATEOAS apropriados.
     */
    RepresentationModel<?> buildApiRootLinks(Authentication authentication);
}