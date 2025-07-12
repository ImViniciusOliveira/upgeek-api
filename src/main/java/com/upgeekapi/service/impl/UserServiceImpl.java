package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.request.UpdatePasswordRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.MultiValidationException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementação da interface {@link UserService}.
 * <p>
 * Esta classe orquestra a lógica de negócio para o gerenciamento de contas de usuário.
 * Ela é responsável por validar as requisições, interagir com o repositório e
 * gerenciar as atualizações através do padrão Builder para um fluxo de dados mais seguro.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return findByIdOrThrow(userId);
    }

    @Override
    @Transactional
    public User updateAccount(Long userId, UpdateAccountRequestDTO request) {
        // 1. Busca a entidade atual.
        User currentUser = findByIdOrThrow(userId);

        // 2. Valida as regras de negócio externas (conflitos de unicidade).
        validateUniqueFieldsOnUpdate(request, userId);

        // 3. Usa o padrão toBuilder() para criar uma cópia atualizada da entidade.
        // A lógica condicional garante que apenas os campos fornecidos no DTO sejam alterados,
        // mantendo os valores originais para os campos não fornecidos.
        User updatedUser = currentUser.toBuilder()
                .username(request.username() != null ? request.username() : currentUser.getUsername())
                .name(request.name() != null ? request.name() : currentUser.getName())
                .email(request.email() != null ? request.email() : currentUser.getEmail())
                .build();

        // 4. Persiste a entidade atualizada. O JPA fará um UPDATE no banco.
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDTO request) {
        User user = findByIdOrThrow(userId);
        user.changePassword(request.currentPassword(), request.newPassword(), passwordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        User userToDelete = findByIdOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
    }

    private void validateUniqueFieldsOnUpdate(UpdateAccountRequestDTO request, Long currentUserId) {
        Map<String, String> errors = new HashMap<>();

        if (request.username() != null && !request.username().isBlank()) {
            userRepository.findByUsername(request.username())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser ->
                            errors.put("username", "O nome de usuário '" + request.username() + "' já está em uso.")
                    );
        }

        if (request.email() != null && !request.email().isBlank()) {
            userRepository.findByEmail(request.email())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser ->
                            errors.put("email", "O email '" + request.email() + "' já está em uso.")
                    );
        }

        if (!errors.isEmpty()) {
            throw new MultiValidationException("Um ou mais campos já estão em uso.", errors);
        }
    }
}