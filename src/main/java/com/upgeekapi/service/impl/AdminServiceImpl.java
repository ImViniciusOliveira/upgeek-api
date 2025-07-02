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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserAccountDTO addRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));

        Role roleToAdd = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role com nome '" + roleName + "' não encontrada."));

        user.getRoles().add(roleToAdd);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserAccountDTO removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));

        Role roleToRemove = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role com nome '" + roleName + "' não encontrada."));

        // Regra de Negócio: Impede que um usuário fique sem nenhuma role.
        if (user.getRoles().size() <= 1 && user.getRoles().contains(roleToRemove)) {
            throw new BusinessRuleException("Não é possível remover a última role de um usuário.");
        }

        user.getRoles().remove(roleToRemove);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}