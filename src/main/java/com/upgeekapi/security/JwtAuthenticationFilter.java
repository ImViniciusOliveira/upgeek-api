package com.upgeekapi.security;

import com.upgeekapi.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filtro que intercepta todas as requisições para validar o token JWT.
 * <p>
 * Ele é o componente que integra nosso sistema de token customizado com o Spring Security,
 * sendo responsável por extrair o token, validá-lo e popular o SecurityContext se o token for válido.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * O coração do filtro. Este método é executado para cada requisição.
     * Ele extrai o token, o valida e, se bem-sucedido, configura a autenticação
     * no contexto de segurança do Spring para a requisição atual.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        getTokenFromHeader(request)
                .flatMap(tokenService::validateToken)
                .ifPresent(principal -> {
                    var authorities = principal.roles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

        // Passa a requisição para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar privado para extrair o token JWT do cabeçalho 'Authorization'.
     *
     * @param request A requisição HTTP recebida.
     * @return um {@link Optional} contendo a string do token puro (sem o prefixo "Bearer "),
     * ou um Optional vazio se o cabeçalho estiver ausente ou malformado.
     */
    private Optional<String> getTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HEADER_AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(header -> header.substring(TOKEN_PREFIX.length()));
    }
}
