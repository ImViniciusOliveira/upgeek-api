package com.upgeekapi.service;

import com.upgeekapi.dto.response.UserAccountDTO;

/**
 * Interface que define o contrato para os serviços de administração,
 * como o gerenciamento de permissões de usuários.
 */
public interface AdminService {

    /**
     * Adiciona uma role a um usuário existente.
     * @param userId O ID do usuário que receberá a role.
     * @param roleName O nome da role a ser adicionada (ex: "ROLE_ADMIN").
     * @return O DTO do usuário com as permissões atualizadas.
     */
    UserAccountDTO addRoleToUser(Long userId, String roleName);

    /**
     * Remove uma role de um usuário existente.
     * @param userId O ID do usuário que terá a role removida.
     * @param roleName O nome da role a ser removida.
     * @return O DTO do usuário com as permissões atualizadas.
     */
    UserAccountDTO removeRoleFromUser(Long userId, String roleName);
}