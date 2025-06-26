package com.upgeekapi.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário autenticado tenta acessar um recurso
 * ou executar uma ação para a qual não possui permissão.
 * Indica que a identidade do usuário é conhecida, mas suas permissões são insuficientes.
 * Mapeia para o status HTTP 403 (Forbidden).
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends RuntimeException {

    /**
     * Constrói uma nova AuthorizationException com a mensagem de detalhe especificada.
     * @param message A mensagem que descreve a falha de autorização.
     */
    public AuthorizationException(String message) {
        super(message);
    }
}