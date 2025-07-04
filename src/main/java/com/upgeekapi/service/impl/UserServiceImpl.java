package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação da interface {@link UserService}.
 * Contém a lógica de negócio para as operações de gerenciamento da conta do usuário autenticado.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserAccountDTO findUserAccountById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
        return userMapper.toDto(user);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * A lógica verifica se os novos 'username' e 'email' já estão em uso por outros usuários
     * antes de aplicar a atualização, garantindo a integridade dos dados.
     */
    @Override
    @Transactional
    public UserAccountDTO updateUser(Long userId, UpdateAccountRequestDTO request) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado para atualização."));

        // Atualiza o nome se fornecido
        if (request.name() != null && !request.name().isBlank()) {
            userToUpdate.setName(request.name());
        }

        // Atualiza o username se fornecido, com verificação de conflito
        if (request.username() != null && !request.username().isBlank()) {
            // Só verifica o conflito se o novo username for diferente do atual
            if (!request.username().equals(userToUpdate.getUsername())) {
                userRepository.findByUsername(request.username()).ifPresent(existingUser -> {
                    throw new DataConflictException("O nome de usuário '" + request.username() + "' já está em uso.");
                });
                userToUpdate.setUsername(request.username());
            }
        }

        // Atualiza o email se fornecido, com verificação de conflito
        if (request.email() != null && !request.email().isBlank()) {
            // Só verifica o conflito se o novo email for diferente do atual
            if (!request.email().equals(userToUpdate.getEmail())) {
                userRepository.findByEmail(request.email()).ifPresent(existingUser -> {
                    throw new DataConflictException("O email '" + request.email() + "' já está em uso.");
                });
                userToUpdate.setEmail(request.email());
            }
        }

        User updatedUser = userRepository.save(userToUpdate);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Não é possível deletar. Usuário com ID '" + userId + "' não encontrado.");
        }
        userRepository.deleteById(userId);
    }
}