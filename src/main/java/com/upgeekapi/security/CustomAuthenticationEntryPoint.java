package com.upgeekapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgeekapi.dto.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
 * Ponto de entrada customizado para tratar falhas de autenticação (erros 401 Unauthorized).
 * <p>
 * Este componente é acionado pelo Spring Security quando um usuário <b>não autenticado</b>
 * (anônimo ou com token inválido) tenta acessar um recurso protegido. Sua função é
 * interceptar a requisição antes que ela seja rejeitada e retornar uma resposta
 * {@link ErrorDTO} padronizada, em vez da página de login padrão do Spring.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    /**
     * Inicia o fluxo de tratamento de falha de autenticação.
     * <p>
     * Este método é invocado quando uma {@link AuthenticationException} é lançada.
     *
     * @param request       A requisição que resultou na falha.
     * @param response      A resposta que será enviada ao cliente.
     * @param authException A exceção que foi lançada.
     * @throws IOException se ocorrer um erro de I/O ao escrever na resposta.
     */
    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException)
            throws IOException {

        // 1. Cria o DTO de erro padronizado para a resposta 401.
        ErrorDTO errorDto = new ErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Acesso não autorizado. As credenciais de autenticação não foram fornecidas ou são inválidas."
        );

        // 2. Configura o cabeçalho da resposta HTTP.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 3. Serializa o DTO de erro para o corpo da resposta.
        OutputStream responseStream = response.getOutputStream();
        mapper.writeValue(responseStream, errorDto);
        responseStream.flush();
    }
}