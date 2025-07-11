package com.upgeekapi.service;

import com.upgeekapi.core.security.AuthPrincipal;
import java.util.Optional;

/**
 * Interface que define o contrato para o serviço de gerenciamento de tokens.
 * Atua como uma FACADE (Padrão GoF), simplificando a interação com o subsistema
 * complexo de criação e validação de JWTs. O resto da aplicação só interage com esta interface.
 */
public interface TokenService {

    /**
     * Gera um novo token JWT com base nos dados do principal.
     * @param principal O objeto contendo os dados do usuário a serem incluídos no token.
     * @return Uma string representando o token JWT assinado.
     */
    String generateToken(AuthPrincipal principal);

    /**
     * Valida uma string de token e, se for válida, extrai os dados do principal.
     * @param token O token JWT como uma string.
     * @return Um Optional contendo o AuthPrincipal se o token for válido, ou um Optional vazio caso contrário.
     */
    Optional<AuthPrincipal> validateToken(String token);
}