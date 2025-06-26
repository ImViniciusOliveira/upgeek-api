package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.CreateUserRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper; // Importa nosso novo Mapper
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementação concreta da interface {@link UserService}.
 * Contém a lógica de negócio principal para operações relacionadas a usuários,
 * delegando a conversão de dados para a camada de Mapper.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Construtor para injeção de dependência. O Spring injetará as implementações
     * de UserRepository e UserMapper automaticamente.
     * @param userRepository O repositório para acesso aos dados do usuário.
     * @param userMapper O mapper para converter a entidade User em DTO.
     */
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccountDTO findUserAccountByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o email '" + email + "' não encontrado."));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccountDTO> findAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toDto(users);
    }

    @Override
    @Transactional
    public UserAccountDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        userRepository.findByEmail(createUserRequestDTO.email()).ifPresent(user -> {
            throw new DataConflictException("O email '" + createUserRequestDTO.email() + "' já está em uso.");
        });

        User newUser = new User(createUserRequestDTO.email(), createUserRequestDTO.name());
        User savedUser = userRepository.save(newUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        User userToDelete = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Não é possível deletar. Usuário com o email '" + email + "' não encontrado."));

        userRepository.delete(userToDelete);
    }
}