package com.upgeekapi.service.validation;

import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Componente especialista responsável por validar a unicidade dos dados de um usuário.
 * Aplica o Princípio da Responsabilidade Única, isolando a lógica de validação
 * de conflitos do serviço de autenticação.
 */
@Component
@RequiredArgsConstructor
public class UserUniquenessValidator {

    private final UserRepository userRepository;

    /**
     * Valida se os dados de um novo registro (email, CPF) já existem no sistema.
     *
     * @param request O DTO com os dados do novo usuário.
     * @throws DataConflictException se um conflito for encontrado.
     */
    public void validate(RegistrationRequestDTO request) {
        checkConflict(
                userRepository.findByEmail(request.email()),
                "O email '" + request.email() + "' já está em uso."
        );
        checkConflict(
                userRepository.findByCpf(request.cpf()),
                "O CPF fornecido já está cadastrado."
        );
    }

    /**
     * Método auxiliar que verifica se um Optional contém um usuário e lança uma exceção
     * se for o caso.
     *
     * @param existingUser O resultado da busca no repositório.
     * @param message A mensagem de erro a ser usada na exceção.
     */
    private void checkConflict(Optional<User> existingUser, String message) {
        if (existingUser.isPresent()) {
            throw new DataConflictException(message);
        }
    }
}