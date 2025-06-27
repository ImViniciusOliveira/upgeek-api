package com.upgeekapi.service;

import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.response.LoginResponseDTO;

/**
 * Interface que define o contrato para os serviços de autenticação.
 */
public interface AuthService {
    /**
     * Autentica um usuário com base nas credenciais e gera um token JWT.
     * @param request DTO contendo o email e a senha.
     * @return um DTO contendo o token JWT.
     * @throws com.upgeekapi.exception.custom.AuthenticationException se as credenciais forem inválidas.
     */
    LoginResponseDTO login(LoginRequestDTO request);
}