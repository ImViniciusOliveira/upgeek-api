package com.upgeekapi.service;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.RoleEnum;

/**
 * Interface que define o contrato para os serviços de administração,
 * como o gerenciamento de permissões de usuários.
 */
public interface AdminService {

    /**
     * Adiciona uma permissão (role) a um usuário existente.
     *
     * @param userId O ID do usuário que receberá a permissão.
     * @param role A permissão a ser adicionada (ex: RoleEnum.ROLE_ADMIN).
     * @return O DTO do usuário com as permissões atualizadas.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     */
    UserAccountDTO addRoleToUser(Long userId, RoleEnum role);

    /**
     * Remove uma permissão (role) de um usuário existente.
     *
     * @param userId O ID do usuário que terá a permissão removida.
     * @param role A permissão a ser removida.
     * @return O DTO do usuário com as permissões atualizadas.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o usuário não for encontrado.
     * @throws com.upgeekapi.exception.custom.BusinessRuleException se a operação tentar remover a última permissão do usuário.
     */
    UserAccountDTO removeRoleFromUser(Long userId, RoleEnum role);
}