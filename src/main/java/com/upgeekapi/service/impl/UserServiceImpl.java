package com.upgeekapi.service.impl;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccountDTO findUserAccountByEmail(String email) {
        // Agora a busca é feita corretamente pelo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        return toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccountDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private UserAccountDTO toDto(User user) {
        String userTitle = "Colecionador Nível " + user.getGamificationLevel();

        return new UserAccountDTO(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getGamificationLevel(),
                user.getExperiencePoints(),
                userTitle
        );
    }
}