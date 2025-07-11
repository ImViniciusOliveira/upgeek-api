package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.request.UpdatePasswordRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.BusinessRuleException;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return findUserByIdOrThrow(userId);
    }

    @Override
    @Transactional
    public User updateUserAccount(Long userId, UpdateAccountRequestDTO request) {
        User userToUpdate = findUserByIdOrThrow(userId);

        // Primeiro, valida se os novos campos únicos entram em conflito com outros usuários.
        validateUniqueFieldsOnUpdate(request, userId);

        // Em seguida, usa o mapper para aplicar apenas os campos não nulos do DTO.
        // Isso substitui os múltiplos métodos "handle...".
        userMapper.updateUserFromDto(request, userToUpdate);

        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDTO request) {
        User user = findUserByIdOrThrow(userId);

        // 1. Verifica se a senha atual fornecida corresponde à senha armazenada.
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessRuleException("A senha atual está incorreta.");
        }

        // 2. (Recomendado) Impede que a nova senha seja igual à antiga.
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BusinessRuleException("A nova senha não pode ser igual à senha atual.");
        }

        // 3. Codifica e define a nova senha.
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // 4. Persiste a alteração.
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User userToDelete = findUserByIdOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    /**
     * Método auxiliar privado para buscar um usuário pelo ID ou lançar uma exceção padrão.
     */
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
    }

    /**
     * Valida se o novo nome de usuário ou email da requisição de atualização já estão em uso por outro usuário.
     *
     * @param request O DTO contendo os novos valores potenciais.
     * @param currentUserId O ID do usuário que está sendo atualizado, para excluí-lo da verificação de conflito.
     */
    private void validateUniqueFieldsOnUpdate(UpdateAccountRequestDTO request, Long currentUserId) {
        if (request.username() != null && !request.username().isBlank()) {
            userRepository.findByUsername(request.username())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser -> {
                        throw new DataConflictException("O nome de usuário '" + request.username() + "' já está em uso.");
                    });
        }

        if (request.email() != null && !request.email().isBlank()) {
            userRepository.findByEmail(request.email())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser -> {
                        throw new DataConflictException("O email '" + request.email() + "' já está em uso.");
                    });
        }
    }
}