package com.upgeekapi.exception.handler;

import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.exception.custom.AuthenticationException;
import com.upgeekapi.exception.custom.BusinessRuleException;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Handler centralizado que captura exceções customizadas lançadas em toda a
 * aplicação e as converte em respostas HTTP padronizadas e consistentes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura a {@link ResourceNotFoundException} e a traduz para uma resposta HTTP 404 Not Found.
     * @param ex A exceção capturada.
     * @return Um ResponseEntity com o status 404 e um corpo de erro padronizado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura a {@link BusinessRuleException} e a traduz para uma resposta HTTP 400 Bad Request.
     * @param ex A exceção capturada.
     * @return Um ResponseEntity com o status 400 e um corpo de erro padronizado.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorDTO> handleBusinessRuleException(BusinessRuleException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura a {@link DataConflictException} e a traduz para uma resposta HTTP 409 Conflict.
     * @param ex A exceção capturada.
     * @return Um ResponseEntity com o status 409 e um corpo de erro padronizado.
     */
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorDTO> handleDataConflictException(DataConflictException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    /**
     * Captura a {@link AuthenticationException} e a traduz para uma resposta HTTP 401 Unauthorized.
     * @param ex A exceção capturada.
     * @return Um ResponseEntity com o status 401 e um corpo de erro padronizado.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Captura a exceção MethodArgumentNotValidException, lançada pelo Spring
     * quando um objeto anotado com @Valid falha na validação.
     * @param ex A exceção contendo todos os erros de validação.
     * @return Um ResponseEntity com o status 400 e uma mensagem de erro clara e formatada.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return "'" + fieldName + "': " + errorMessage;
                })
                .collect(Collectors.joining("; "));

        return buildErrorResponse(
                new RuntimeException("Erro de validação: " + errors),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Método template privado que centraliza a lógica de criação de respostas de erro.
     * @param ex A exceção original para extrair a mensagem.
     * @param status O HttpStatus a ser usado na resposta.
     * @return Um ResponseEntity contendo o ErrorDTO padronizado.
     */
    private ResponseEntity<ErrorDTO> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDto, status);
    }
}