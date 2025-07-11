package com.upgeekapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgeekapi.dto.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

/**
 * Handler customizado para tratar falhas de autorização (erros 403 Forbidden).
 * <p>
 * Este componente é acionado pelo Spring Security quando um usuário <b>já autenticado</b>
 * tenta acessar um recurso para o qual ele não possui as permissões (roles/authorities)
 * necessárias. Ele se diferencia do {@code AuthenticationEntryPoint}, que trata falhas de
 * <b>autenticação</b> (erros 401 Unauthorized) para usuários anônimos.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    /**
     * Lida com a exceção de acesso negado, construindo uma resposta JSON padronizada.
     *
     * @param request               A requisição que resultou na falha.
     * @param response              A resposta que será enviada ao cliente.
     * @param accessDeniedException A exceção que foi lançada.
     * @throws IOException se ocorrer um erro de I/O ao escrever na resposta.
     */
    @Override
    public void handle(@NonNull HttpServletRequest request,
                       @NonNull HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException)
            throws IOException {

        // 1. Cria o DTO de erro padronizado para a resposta 403.
        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Acesso negado. Você não possui as permissões necessárias para acessar este recurso."
        );

        // 2. Configura o cabeçalho da resposta HTTP.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 3. Serializa o DTO de erro para o corpo da resposta.
        OutputStream responseStream = response.getOutputStream();
        mapper.writeValue(responseStream, errorDto);
        responseStream.flush();
    }
}