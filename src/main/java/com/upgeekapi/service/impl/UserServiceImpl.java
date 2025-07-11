package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.request.UpdatePasswordRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.MultiValidationException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.UserMapper;
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
 * delegar a manipulação de estado para a própria entidade {@link User} ou para o {@link UserMapper}.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return findByIdOrThrow(userId);
    }

    @Override
    @Transactional
    public User updateAccount(Long userId, UpdateAccountRequestDTO request) {
        // 1. Busca a entidade que será atualizada.
        User userToUpdate = findByIdOrThrow(userId);

        // 2. Valida se os novos dados únicos (email, username) não conflitam com outros usuários.
        //    Esta validação coleta todos os erros e os lança de uma vez.
        validateUniqueFieldsOnUpdate(request, userId);

        // 3. Delega a aplicação dos dados do DTO para a entidade através do MapStruct.
        userMapper.updateUserFromDto(request, userToUpdate);

        // 4. Persiste a entidade atualizada.
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDTO request) {
        // 1. Busca o usuário.
        User user = findByIdOrThrow(userId);

        // 2. Delega a lógica de validação e alteração da senha para a própria entidade.
        //    O método changePassword contém as regras de negócio e lança exceções se forem violadas.
        user.changePassword(request.currentPassword(), request.newPassword(), passwordEncoder);

        // 3. Persiste a alteração (o save é necessário porque o estado do objeto 'user' foi modificado).
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        // Garante que o usuário exista antes de tentar deletar.
        User userToDelete = findByIdOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    /**
     * Método auxiliar privado para buscar um usuário pelo ID ou lançar uma exceção padrão.
     * Centraliza a lógica de "encontrar ou falhar", mantendo o código dos métodos públicos mais limpo.
     *
     * @param userId O ID do usuário a ser buscado.
     * @return A entidade {@link User} encontrada.
     * @throws ResourceNotFoundException se o usuário não for encontrado.
     */
    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + userId + "' não encontrado."));
    }

    /**
     * Valida se o novo nome de usuário ou email da requisição de atualização já estão em uso por outro usuário.
     * <p>
     * Coleta todos os erros de conflito encontrados e os lança de uma só vez em uma
     * {@link MultiValidationException} para uma melhor experiência do cliente.
     *
     * @param request O DTO contendo os novos valores potenciais.
     * @param currentUserId O ID do usuário que está sendo atualizado, para excluí-lo da verificação de conflito.
     */
    private void validateUniqueFieldsOnUpdate(UpdateAccountRequestDTO request, Long currentUserId) {
        Map<String, String> errors = new HashMap<>();

        // Verifica o conflito de nome de usuário, se um novo nome foi fornecido.
        if (request.username() != null && !request.username().isBlank()) {
            userRepository.findByUsername(request.username())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser ->
                            errors.put("username", "O nome de usuário '" + request.username() + "' já está em uso.")
                    );
        }

        // Verifica o conflito de email, se um novo email foi fornecido.
        if (request.email() != null && !request.email().isBlank()) {
            userRepository.findByEmail(request.email())
                    .filter(foundUser -> !foundUser.getId().equals(currentUserId))
                    .ifPresent(existingUser ->
                            errors.put("email", "O email '" + request.email() + "' já está em uso.")
                    );
        }

        // Se o mapa de erros não estiver vazio, lança a exceção consolidada.
        if (!errors.isEmpty()) {
            throw new MultiValidationException("Um ou mais campos já estão em uso.", errors);
        }
    }
}