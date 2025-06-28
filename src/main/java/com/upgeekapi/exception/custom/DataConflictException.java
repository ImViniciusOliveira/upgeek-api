package com.upgeekapi.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma tentativa de criar um novo recurso entra em
 * conflito com um recurso que já existe no sistema, violando uma restrição de unicidade.
 * <p>
 * Mapeia para o status HTTP 409 (Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflictException extends RuntimeException {

    /**
     * Constrói uma nova DataConflictException com a mensagem de detalhe especificada.
     * @param message A mensagem que descreve o conflito de dados ocorrido (ex: "O email '...' já está em uso.").
     */
    public DataConflictException(String message) {
        super(message);
    }
}
