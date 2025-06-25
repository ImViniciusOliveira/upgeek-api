package com.upgeekapi.service;

import com.upgeekapi.dto.response.UserAccountDTO;
import java.util.List;

public interface UserService {

    /**
     * Busca os dados da conta de um usuário com base no seu email.
     * @param email O email do usuário a ser buscado.
     * @return Um DTO com os dados da conta do usuário.
     */
    UserAccountDTO findUserAccountByEmail(String email);

    /**
     * Busca todos os usuários cadastrados no sistema.
     * @return Uma lista de DTOs com os dados de todas as contas de usuário.
     */
    List<UserAccountDTO> findAllUsers();
}
