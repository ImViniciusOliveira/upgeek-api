package com.upgeekapi.service.impl;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.Role;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.BusinessRuleException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.RoleRepository;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserAccountDTO addRoleToUser(Long userId, String roleName) {
        UserAndRole userAndRole = findUserAndRole(userId, roleName);
        User user = userAndRole.user();
        Role roleToAdd = userAndRole.role();

        if (user.getRoles().contains(roleToAdd)) {
            return userMapper.toDto(user);
        }

        user.getRoles().add(roleToAdd);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserAccountDTO removeRoleFromUser(Long userId, String roleName) {
        UserAndRole userAndRole = findUserAndRole(userId, roleName);
        User user = userAndRole.user();
        Role roleToRemove = userAndRole.role();

        if (user.getRoles().size() <= 1 && user.getRoles().contains(roleToRemove)) {
            throw new BusinessRuleException("Não é possível remover a última role de um usuário.");
        }

        user.getRoles().remove(roleToRemove);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Helper record to hold a user and a role together.
     */
    private record UserAndRole(User user, Role role) {}

    /**
     * Private helper method to find both the user and the role, reducing code duplication.
     *
     * @param userId   The ID of the user to find.
     * @param roleName The name of the role to find.
     * @return A record containing the found User and Role entities.
     */
    private UserAndRole findUserAndRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role com nome '" + roleName + "' não encontrada."));

        return new UserAndRole(user, role);
    }
}
