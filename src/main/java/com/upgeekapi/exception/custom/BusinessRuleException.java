package com.upgeekapi.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma operação viola uma regra de negócio definida pela aplicação.
 * <p>
 * Indica que a requisição do cliente era sintaticamente válida, mas não pôde ser
 * processada por uma restrição lógica (ex: estoque insuficiente, pré-requisito não atendido).
 * Mapeia para o status HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException {

    /**
     * Constrói uma nova BusinessRuleException com a mensagem de detalhe especificada.
     * @param message A mensagem descritiva da regra de negócio que foi violada.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}