package com.upgeekapi.service;

import com.upgeekapi.dto.request.CreateUserRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import java.util.List;

/**
 * Interface que define o contrato para os serviços relacionados a usuários.
 * Desacopla a camada de controller da implementação da lógica de negócio.
 */
public interface UserService {

    /**
     * Encontra os dados de uma conta de usuário pelo seu email.
     *
     * @param email O endereço de email único associado ao usuário.
     * @return Um {@link UserAccountDTO} contendo os dados públicos e de gamificação do usuário.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se nenhum usuário for encontrado com o email fornecido.
     */
    UserAccountDTO findUserAccountByEmail(String email);

    /**
     * Retorna uma lista de todas as contas de usuário registradas.
     * <p>
     * <strong>Atenção:</strong> Em um ambiente de produção, este método deve ser substituído
     * por uma versão paginada para evitar problemas de performance com um grande volume de dados.
     *
     * @return uma lista de {@link UserAccountDTO}. A lista estará vazia se não houver usuários.
     */
    List<UserAccountDTO> findAllUsers();

    /**
     * Cria um novo usuário no sistema.
     * @param createUserRequestDTO Os dados para a criação do novo usuário.
     * @return um {@link UserAccountDTO} representando o usuário recém-criado.
     * @throws com.upgeekapi.exception.custom.DataConflictException se o email fornecido já estiver em uso.
     */
    UserAccountDTO createUser(CreateUserRequestDTO createUserRequestDTO);

    /**
     * Deleta um usuário do sistema com base no seu email.
     * @param email O email do usuário a ser deletado.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se nenhum usuário for encontrado com o email fornecido.
     */
    void deleteUserByEmail(String email);
}