package com.upgeekapi.exception.custom;

import lombok.Getter;
import java.util.Map;

/**
 * Exceção usada para sinalizar que múltiplas regras de validação de negócio falharam.
 * Ela carrega um mapa de erros específicos de campo, permitindo uma resposta de erro
 * detalhada e consolidada para o cliente, consistente com as falhas de validação de DTO.
 */
@Getter
public class MultiValidationException extends RuntimeException {

    /**
     * Um mapa onde a chave é o nome do campo/parâmetro inválido e
     * o valor é a mensagem de erro correspondente.
     */
    private final Map<String, String> errors;

    public MultiValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}