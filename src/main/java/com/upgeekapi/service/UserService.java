package com.upgeekapi.service;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.request.UpdatePasswordRequestDTO;
import com.upgeekapi.entity.User; // MUDANÇA: Importamos a entidade

/**
 * Interface que define o contrato para os serviços relacionados ao gerenciamento
 * da conta do usuário. Os métodos retornam a entidade User para desacoplar
 * a camada de serviço da camada de apresentação (DTOs).
 */
public interface UserService {

    /**
     * Encontra um usuário pelo seu ID interno.
     * @param userId O ID único do usuário.
     * @return A entidade {@link User} correspondente.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    User findUserById(Long userId); // MUDANÇA: Retorna a entidade User

    /**
     * Atualiza os dados de um usuário existente.
     * @param userId O ID do usuário a ser atualizado.
     * @param request O DTO com as novas informações a serem aplicadas.
     * @return A entidade {@link User} com os dados atualizados.
     */
    User updateUserAccount(Long userId, UpdateAccountRequestDTO request); // MUDANÇA: Retorna a entidade User

    /**
     * Deleta a conta de um usuário com base no seu ID.
     * @param userId O ID do usuário a ser deletado.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    void deleteUserById(Long userId);

    /**
     * Atualiza a senha de um usuário, após verificar a senha atual.
     *
     * @param userId O ID do usuário cuja senha será alterada.
     * @param request O DTO contendo a senha atual e a nova senha.
     * @throws com.upgeekapi.exception.custom.BusinessRuleException se a senha atual estiver incorreta.
     */
    void updatePassword(Long userId, UpdatePasswordRequestDTO request);
}