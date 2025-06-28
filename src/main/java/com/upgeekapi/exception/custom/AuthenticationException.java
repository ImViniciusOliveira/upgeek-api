package com.upgeekapi.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando as credenciais fornecidas para autenticação
 * são inválidas, ausentes ou expiradas. Indica que o sistema não consegue
 * verificar a identidade do solicitante.
 * <p>
 * Mapeia para o status HTTP 401 (Unauthorized).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    /**
     * Constrói uma nova AuthenticationException com a mensagem de detalhe especificada.
     * @param message A mensagem que descreve a falha na autenticação (ex: "Credenciais inválidas.").
     */
    public AuthenticationException(String message) {
        super(message);
    }
}