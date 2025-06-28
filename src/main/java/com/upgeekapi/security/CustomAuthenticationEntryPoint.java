package com.upgeekapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgeekapi.dto.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

/**
 * Ponto de entrada customizado para tratar falhas de autenticação (erros 401).
 * <p>
 * Esta classe é acionada pelo Spring Security quando uma requisição não autenticada
 * tenta acessar um recurso protegido. Sua função é substituir a resposta padrão
 * por um corpo de erro JSON padronizado.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    /**
     * Construtor que injeta o ObjectMapper gerenciado pelo Spring.
     * Reutilizar o bean do ObjectMapper é mais eficiente do que criar uma nova instância a cada requisição.
     * @param mapper O ObjectMapper configurado pelo Spring.
     */
    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException)
            throws IOException {

        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Acesso não autorizado. Autenticação via Bearer Token é necessária."
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream responseStream = response.getOutputStream();
        mapper.writeValue(responseStream, errorDto);
        responseStream.flush();
    }
}
