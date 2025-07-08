package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // private final UserMapper userMapper; // REMOVIDO: A conversão para DTO não é mais responsabilidade do serviço.

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long userId) { // MUDANÇA: Assinatura do método atualizada
        return findUserByIdOrThrow(userId); // MUDANÇA: Retorna a entidade diretamente
    }

    @Override
    @Transactional
    public User updateUserAccount(Long userId, UpdateAccountRequestDTO request) { // MUDANÇA: Assinatura do método atualizada
        // 1. Busca o usuário que será atualizado
        User userToUpdate = findUserByIdOrThrow(userId);

        // 2. Delega a lógica de atualização para métodos auxiliares (nenhuma mudança aqui)
        handleNameUpdate(userToUpdate, request.name());
        handleUsernameUpdate(userToUpdate, request.username());
        handleEmailUpdate(userToUpdate, request.email());

        // 3. Salva e retorna a entidade atualizada
        return userRepository.save(userToUpdate); // MUDANÇA: Retorna a entidade diretamente
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User userToDelete = findUserByIdOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    /**
     * Método auxiliar que busca um usuário pelo ID ou lança uma exceção.
     */
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
    }

    /**
     * Atualiza o nome do usuário, se um novo nome for fornecido.
     */
    private void handleNameUpdate(User user, String newName) {
        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }
    }

    /**
     * Atualiza o nome de usuário, validando se o novo nome já está em uso.
     */
    private void handleUsernameUpdate(User user, String newUsername) {
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            userRepository.findByUsername(newUsername).ifPresent(existingUser -> {
                throw new DataConflictException("O nome de usuário '" + newUsername + "' já está em uso.");
            });
            user.setUsername(newUsername);
        }
    }

    /**
     * Atualiza o email do usuário, validando se o novo email já está em uso.
     */
    private void handleEmailUpdate(User user, String newEmail) {
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
                throw new DataConflictException("O email '" + newEmail + "' já está em uso.");
            });
            user.setEmail(newEmail);
        }
    }
}