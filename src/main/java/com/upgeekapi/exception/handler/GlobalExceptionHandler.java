package com.upgeekapi.exception.handler;

import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Componente central de tratamento de exceções para a API.
 * <p>
 * Atua como uma barreira entre a lógica de negócio e o cliente, garantindo que todas as falhas,
 * esperadas ou não, sejam traduzidas em respostas de erro {@link ErrorDTO} padronizadas.
 * Isso estabiliza o contrato da API e melhora a experiência do desenvolvedor cliente.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura a {@link ResourceNotFoundException} e a traduz para uma resposta HTTP 404 Not Found.
     * @param ex A exceção que indica que um recurso específico não foi encontrado.
     * @return Um ResponseEntity com o status 404 e um corpo de erro padronizado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura a {@link BusinessRuleException} e a traduz para uma resposta HTTP 400 Bad Request.
     * @param ex A exceção que indica a violação de uma regra de negócio.
     * @return Um ResponseEntity com o status 400 e um corpo de erro padronizado.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorDTO> handleBusinessRuleException(BusinessRuleException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura a {@link DataConflictException} e a traduz para uma resposta HTTP 409 Conflict.
     * @param ex A exceção que indica um conflito de dados, como uma violação de unicidade.
     * @return Um ResponseEntity com o status 409 e um corpo de erro padronizado.
     */
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorDTO> handleDataConflictException(DataConflictException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    /**
     * Captura a {@link AuthenticationException} e a traduz para uma resposta HTTP 401 Unauthorized.
     * @param ex A exceção que indica uma falha na autenticação.
     * @return Um ResponseEntity com o status 401 e um corpo de erro padronizado.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Captura a {@link MethodArgumentNotValidException}, lançada pelo Spring quando a validação de um DTO falha.
     * Transforma os erros de validação (tanto de campo quanto de classe) em uma resposta estruturada.
     *
     * @param ex A exceção contendo todos os erros de validação de DTO.
     * @return Um ResponseEntity com status 400 e um corpo de erro estruturado.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Usamos um HashMap para poder adicionar os dois tipos de erro.
        Map<String, String> errors = new HashMap<>();

        // 1. Adiciona os erros de campo (field-level), como @NotBlank, @Size, etc.
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 2. Adiciona os erros de classe (class-level/global), como a sua @ValidPriceRange.
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            // Para erros globais, usamos o nome do objeto como chave para clareza.
            String objectName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(objectName, errorMessage);
        });

        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Um ou mais campos falharam na validação.",
                errors // <-- Usamos o novo mapa combinado
        );

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Lida com a {@link MultiValidationException} para fornecer um formato de resposta de erro
     * consistente para validações de lógica de negócio que envolvem múltiplos campos.
     *
     * @param ex A exceção contendo o mapa de erros de validação de negócio.
     * @return Um ResponseEntity com status 400 Bad Request e um corpo de erro detalhado.
     */
    @ExceptionHandler(MultiValidationException.class)
    public ResponseEntity<ErrorDTO> handleMultiValidationException(MultiValidationException ex) {
        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                ex.getErrors()
        );
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler específico para rotas não encontradas (erros 404).
     * Captura a exceção do Spring para URLs que não correspondem a nenhum endpoint mapeado.
     *
     * @param ex A exceção NoResourceFoundException capturada.
     * @return Um ResponseEntity com o status 404 e uma mensagem clara.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoResourceFound(NoResourceFoundException ex) {
        String errorMessage = "O endpoint solicitado não foi encontrado: " + ex.getResourcePath();
        return buildErrorResponse(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura a HttpMessageNotReadableException, comumente lançada quando o corpo da requisição
     * é um JSON malformado ou inválido.
     *
     * @param ex A exceção que indica que a requisição não pôde ser lida.
     * @return Um ResponseEntity com o status 400 e uma mensagem clara sobre o erro de sintaxe.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleMalformedJson(HttpMessageNotReadableException ex) {
        String userFriendlyMessage = "A requisição contém um JSON malformado ou com sintaxe inválida. Por favor, verifique o corpo da requisição.";
        log.warn("Requisição rejeitada por JSON malformado. Causa: {}", ex.getMessage());
        return buildErrorResponse(userFriendlyMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler de último recurso ("catch-all") para exceções inesperadas (erros 500).
     * <p>
     * Este é o handler mais importante para a segurança e estabilidade da API. Ele garante que
     * nenhuma exceção não tratada vaze detalhes de implementação para o cliente.
     *
     * @param ex A exceção genérica capturada.
     * @return Um ResponseEntity com o status 500 e uma mensagem de erro genérica e segura.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        // Loga o erro completo com stack trace para depuração interna.
        log.error("Ocorreu um erro inesperado no servidor.", ex);

        // Retorna uma mensagem genérica e segura para o cliente, sem expor detalhes internos.
        String safeMessage = "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.";
        return buildErrorResponse(safeMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Método de fábrica privado que centraliza a criação de respostas de erro simples.
     *
     * @param message A mensagem de erro a ser exibida.
     * @param status  O HttpStatus a ser usado na resposta.
     * @return Um ResponseEntity contendo o ErrorDTO padronizado.
     */
    private ResponseEntity<ErrorDTO> buildErrorResponse(String message, HttpStatus status) {
        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(errorDto, status);
    }

    /**
     * Sobrecarga do método de fábrica para aceitar uma exceção diretamente.
     */
    private ResponseEntity<ErrorDTO> buildErrorResponse(Exception ex, HttpStatus status) {
        return buildErrorResponse(ex.getMessage(), status);
    }
}