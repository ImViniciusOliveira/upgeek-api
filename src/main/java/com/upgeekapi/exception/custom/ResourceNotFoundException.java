package com.upgeekapi.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma busca por um recurso específico (ex: User, Product)
 * não retorna nenhum resultado.
 * <p>
 * Indica que o recurso solicitado não existe no sistema.
 * Mapeia para o status HTTP 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constrói uma nova ResourceNotFoundException com a mensagem de detalhe especificada.
     * @param message A mensagem que descreve qual recurso e identificador não foram encontrados.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}