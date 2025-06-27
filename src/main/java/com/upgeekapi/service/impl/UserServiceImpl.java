package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.CreateUserRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementação da interface {@link UserService}.
 * Contém a lógica de negócio para usuários, delegando a conversão
 * de dados para a camada de Mapper e tratando a criptografia de senhas.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor para injeção de dependência. O Spring injetará todas as dependências necessárias.
     * @param userRepository O repositório para acesso aos dados do usuário.
     * @param userMapper O mapper para converter a entidade User em DTO.
     * @param passwordEncoder O codificador para criptografar e verificar senhas.
     */
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

        // Criptografa a senha do usuário antes de salvá-la no banco de dados.
        String hashedPassword = passwordEncoder.encode(createUserRequestDTO.password());

        User newUser = new User(
                createUserRequestDTO.email(),
                createUserRequestDTO.name(),
                hashedPassword // Salva a senha já criptografada
        );

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