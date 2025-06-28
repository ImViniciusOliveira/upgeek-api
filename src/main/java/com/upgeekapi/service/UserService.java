package com.upgeekapi.service;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;

/**
 * Interface que define o contrato para os serviços relacionados ao gerenciamento
 * da conta do usuário.
 */
public interface UserService {

    /**
     * Encontra os dados de uma conta de usuário pelo seu ID interno.
     * @param userId O ID único do usuário.
     * @return Um DTO com os dados da conta do usuário.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    UserAccountDTO findUserAccountById(Long userId);

    /**
     * Atualiza os dados de um usuário existente.
     * @param userId O ID do usuário a ser atualizado.
     * @param request O DTO com as novas informações a serem aplicadas.
     * @return O DTO do usuário com os dados atualizados.
     */
    UserAccountDTO updateUser(Long userId, UpdateAccountRequestDTO request);

    /**
     * Deleta a conta de um usuário com base no seu ID.
     * @param userId O ID do usuário a ser deletado.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    void deleteUserById(Long userId);
}