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

        // A operação já é idempotente. Se a role já existe, não fazemos nada.
        // O método Set.add() já retorna false se o elemento existe, então podemos usá-lo.
        boolean wasAdded = user.getRoles().add(roleToAdd);

        // Só salvamos no banco se houve uma mudança real.
        if (wasAdded) {
            return saveAndMap(user);
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserAccountDTO removeRoleFromUser(Long userId, String roleName) {
        UserAndRole userAndRole = findUserAndRole(userId, roleName);
        User user = userAndRole.user();
        Role roleToRemove = userAndRole.role();

        // A regra de negócio de não remover a última role é a verificação mais importante.
        if (user.getRoles().size() <= 1 && user.getRoles().contains(roleToRemove)) {
            throw new BusinessRuleException("Não é possível remover a última role de um usuário.");
        }

        // MELHORIA: Tornamos a remoção idempotente.
        // O método Set.remove() retorna true se o elemento foi de fato removido.
        boolean wasRemoved = user.getRoles().remove(roleToRemove);

        // Só salvamos no banco se houve uma mudança real.
        if (wasRemoved) {
            return saveAndMap(user);
        }

        // Se a role não existia no usuário, simplesmente retornamos o estado atual.
        return userMapper.toDto(user);
    }

    /**
     * Helper record to hold a user and a role together.
     */
    private record UserAndRole(User user, Role role) {}

    /**
     * Private helper method to find both the user and the role, reducing code duplication.
     */
    private UserAndRole findUserAndRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role com nome '" + roleName + "' não encontrada."));

        return new UserAndRole(user, role);
    }

    /**
     * Método auxiliar para salvar a entidade User e mapeá-la para um DTO.
     * Reduz a duplicação nos métodos públicos.
     */
    private UserAccountDTO saveAndMap(User user) {
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}