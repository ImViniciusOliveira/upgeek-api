package com.upgeekapi.service.impl;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.BusinessRuleException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do serviço de administração {@link AdminService}.
 * <p>
 * Esta classe contém a lógica de negócio para operações administrativas,
 * como a manipulação de papéis (roles) de usuários. Ela garante que as
 * operações sejam seguras, idempotentes e sigam as regras de negócio definidas.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação é idempotente: se o usuário já possuir a permissão,
     * nenhuma alteração será feita e o estado atual do usuário será retornado.
     */
    @Override
    @Transactional
    public UserAccountDTO addRoleToUser(Long userId, RoleEnum role) {
        // 1. Busca a entidade que será modificada.
        User user = findUserOrThrow(userId);

        // 2. Usa o retorno do método Set.add() para verificar se o estado foi alterado.
        // Isso garante a idempotência da operação.
        boolean wasAdded = user.getRoles().add(role);

        // 3. Persiste e mapeia a resposta apenas se a permissão foi de fato adicionada.
        if (wasAdded) {
            return saveAndMap(user);
        }

        // Se a permissão já existia, retorna o estado atual sem acessar o banco de dados.
        return userMapper.toDto(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação é idempotente e protege contra a remoção da última
     * permissão de um usuário, lançando uma {@link BusinessRuleException} nesse caso.
     */
    @Override
    @Transactional
    public UserAccountDTO removeRoleFromUser(Long userId, RoleEnum role) {
        // 1. Busca a entidade que será modificada.
        User user = findUserOrThrow(userId);

        // 2. Aplica a regra de negócio que impede um usuário de ficar sem permissões.
        if (user.getRoles().size() <= 1 && user.getRoles().contains(role)) {
            throw new BusinessRuleException("Não é possível remover a última permissão de um usuário.");
        }

        // 3. Usa o retorno de Set.remove() para verificar se o estado foi alterado.
        boolean wasRemoved = user.getRoles().remove(role);

        // 4. Persiste e mapeia a resposta apenas se a permissão foi de fato removida.
        if (wasRemoved) {
            return saveAndMap(user);
        }

        // Se a permissão não existia, retorna o estado atual sem acessar o banco de dados.
        return userMapper.toDto(user);
    }

    /**
     * Busca um usuário pelo ID ou lança uma exceção se não for encontrado.
     * Centraliza a lógica de busca e tratamento de erro de "não encontrado".
     *
     * @param userId O ID do usuário a ser buscado.
     * @return A entidade {@link User} encontrada.
     * @throws ResourceNotFoundException se o usuário não existir.
     */
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
    }

    /**
     * Persiste as alterações na entidade User e a mapeia para um DTO de resposta.
     *
     * @param user A entidade User com o estado a ser salvo.
     * @return O {@link UserAccountDTO} representando o estado atualizado do usuário.
     */
    private UserAccountDTO saveAndMap(User user) {
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}