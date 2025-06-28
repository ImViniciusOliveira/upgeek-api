package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação da interface {@link UserService}.
 * Contém a lógica de negócio para as operações de gerenciamento da conta do usuário.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccountDTO findUserAccountById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserAccountDTO updateUser(Long userId, UpdateAccountRequestDTO request) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado para atualização."));

        // Atualiza apenas os campos permitidos
        if (request.name() != null && !request.name().isBlank()) {
            userToUpdate.setName(request.name());
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