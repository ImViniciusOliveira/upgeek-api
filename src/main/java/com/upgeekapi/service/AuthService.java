package com.upgeekapi.service;

import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.LoginDTO;

/**
 * Interface que define o contrato para os serviços de autenticação.
 * Desacopla a camada de controller da implementação da lógica de negócio
 * para registro e login de usuários.
 */
public interface AuthService {

    /**
     * Autentica um usuário com base nas credenciais e gera um token JWT.
     * @param request DTO contendo o email e a senha.
     * @return um {@link LoginDTO} contendo o token JWT.
     * @throws com.upgeekapi.exception.custom.AuthenticationException se as credenciais forem inválidas.
     */
    LoginDTO login(LoginRequestDTO request);

    /**
     * Registra um novo usuário no sistema.
     * @param request DTO contendo os dados do novo usuário.
     * @throws com.upgeekapi.exception.custom.DataConflictException se o email, username ou CPF já estiverem em uso.
     */
    void register(RegistrationRequestDTO request);
}