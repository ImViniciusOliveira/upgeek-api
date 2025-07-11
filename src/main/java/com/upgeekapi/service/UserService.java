package com.upgeekapi.service;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.request.UpdatePasswordRequestDTO;
import com.upgeekapi.entity.User;

/**
 * Interface que define o contrato para os serviços de gerenciamento da conta do usuário.
 * <p>
 * Esta camada é responsável pela lógica de negócio e opera exclusivamente
 * com a entidade {@link User}. A conversão para DTOs de resposta é delegada
 * para a camada de apresentação (Controller/Assembler).
 */
public interface UserService {

    /**
     * Busca e retorna uma entidade de usuário pelo seu ID.
     *
     * @param userId O ID único do usuário.
     * @return A entidade {@link User} correspondente.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    User findById(Long userId);

    /**
     * Atualiza os dados de perfil de um usuário existente.
     *
     * @param userId O ID do usuário a ser atualizado.
     * @param request O DTO com as novas informações a serem aplicadas.
     * @return A entidade {@link User} com os dados atualizados.
     * @throws com.upgeekapi.exception.custom.DataConflictException se o novo email ou username já estiver em uso.
     */
    User updateAccount(Long userId, UpdateAccountRequestDTO request);

    /**
     * Atualiza a senha de um usuário, após verificar a senha atual.
     *
     * @param userId O ID do usuário cuja senha será alterada.
     * @param request O DTO contendo a senha atual e a nova senha.
     * @throws com.upgeekapi.exception.custom.BusinessRuleException se a senha atual estiver incorreta ou for igual à nova.
     */
    void updatePassword(Long userId, UpdatePasswordRequestDTO request);

    /**
     * Deleta permanentemente a conta de um usuário com base no seu ID.
     *
     * @param userId O ID do usuário a ser deletado.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    void deleteById(Long userId);
}