package com.upgeekapi.service.validation;

import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.exception.custom.MultiValidationException;
import com.upgeekapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Componente especialista responsável por validar a unicidade dos dados de um usuário.
 * <p>
 * Aplica o Princípio da Responsabilidade Única, isolando a lógica de validação
 * de conflitos do serviço de autenticação. Este validador é projetado para
 * coletar todos os erros de unicidade e reportá-los de uma só vez.
 */
@Component
@RequiredArgsConstructor
public class UserUniquenessValidator {

    private final UserRepository userRepository;

    /**
     * Valida se os dados de um novo registro (email, CPF) já existem no sistema,
     * coletando todos os conflitos encontrados.
     *
     * @param request O DTO com os dados do novo usuário.
     * @throws MultiValidationException se um ou mais conflitos forem encontrados.
     */
    public void validate(RegistrationRequestDTO request) {
        Map<String, String> errors = new HashMap<>();

        // Verifica se o email já está em uso e, se estiver, adiciona ao mapa de erros.
        userRepository.findByEmail(request.email())
                .ifPresent(user -> errors.put("email", "O email '" + request.email() + "' já está em uso."));

        // Verifica se o CPF já está cadastrado e, se estiver, adiciona ao mapa de erros.
        userRepository.findByCpf(request.cpf())
                .ifPresent(user -> errors.put("cpf", "O CPF fornecido já está cadastrado."));

        // Se o mapa de erros não estiver vazio, lança uma única exceção com todos os problemas.
        if (!errors.isEmpty()) {
            throw new MultiValidationException("Um ou mais campos já estão em uso.", errors);
        }
    }
}