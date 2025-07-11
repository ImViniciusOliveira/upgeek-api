package com.upgeekapi.service.impl;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.BusinessRuleException;
import com.upgeekapi.exception.custom.MultiValidationException;
import com.upgeekapi.mapper.UserMapper;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação do serviço de administração {@link AdminService}.
 * <p>
 * Esta classe contém a lógica de negócio para operações administrativas,
 * como a manipulação de papéis (roles) de usuários. Ela garante que as
 * operações sejam seguras, idempotentes e sigam as regras de negócio definidas.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação primeiro valida todas as entradas. Se o usuário já possuir o papel,
     * nenhuma alteração será feita e o estado atual do usuário será retornado (idempotência).
     *
     * @param userId O ID do usuário que receberá a role.
     * @param roleName O nome da role a ser adicionada (ex: "ADMIN").
     * @return O DTO do usuário com as permissões atualizadas.
     * @throws MultiValidationException se o ID do usuário ou o nome da role forem inválidos.
     */
    @Override
    @Transactional
    public UserAccountDTO addRoleToUser(Long userId, String roleName) {
        ValidatedInput validated = findAndValidateInputs(userId, roleName);

        boolean wasAdded = validated.user().getRoles().add(validated.role());

        if (wasAdded) {
            return saveAndMap(validated.user());
        }

        return userMapper.toDto(validated.user());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação é idempotente e protege contra a remoção do último
     * papel de um usuário, lançando uma {@link BusinessRuleException} nesse caso.
     *
     * @param userId O ID do usuário que terá a role removida.
     * @param roleName O nome da role a ser removida.
     * @return O DTO do usuário com as permissões atualizadas.
     * @throws MultiValidationException se o ID do usuário ou o nome da role forem inválidos.
     * @throws BusinessRuleException se a operação tentar remover a última role do usuário.
     */
    @Override
    @Transactional
    public UserAccountDTO removeRoleFromUser(Long userId, String roleName) {
        ValidatedInput validated = findAndValidateInputs(userId, roleName);
        User user = validated.user();
        RoleEnum roleToRemove = validated.role();

        if (user.getRoles().size() <= 1 && user.getRoles().contains(roleToRemove)) {
            throw new BusinessRuleException("Não é possível remover a última role de um usuário.");
        }

        boolean wasRemoved = user.getRoles().remove(roleToRemove);

        if (wasRemoved) {
            return saveAndMap(user);
        }

        return userMapper.toDto(user);
    }

    /**
     * Valida o ID do usuário e o nome da role, coletando todos os erros antes de lançar uma exceção.
     * Este método garante que o cliente receba um feedback completo sobre as entradas inválidas.
     *
     * @param userId O ID do usuário a ser validado.
     * @param roleName O nome da role a ser validada.
     * @return Um record {@link ValidatedInput} contendo o User e a RoleEnum se ambos forem válidos.
     * @throws MultiValidationException se uma ou ambas as entradas forem inválidas.
     */
    private ValidatedInput findAndValidateInputs(Long userId, String roleName) {
        // 1. Primeiro, tenta recuperar todos os dados.
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<RoleEnum> roleOpt = validateRoleName(roleName);

        // 2. Em seguida, constrói o mapa de erros com base nos resultados.
        Map<String, String> errors = new HashMap<>();
        if (userOpt.isEmpty()) {
            errors.put("userId", "Usuário com ID '" + userId + "' não encontrado.");
        }
        if (roleOpt.isEmpty()) {
            errors.put("roleName", "Role com nome '" + roleName + "' é inválida.");
        }

        // 3. Se o mapa de erros não estiver vazio, lança a exceção com todos os erros coletados.
        if (!errors.isEmpty()) {
            throw new MultiValidationException("Um ou mais parâmetros são inválidos.", errors);
        }

        // 4. Neste ponto, é garantido que ambos os Optionals contêm valores. As chamadas .get() são seguras.
        return new ValidatedInput(userOpt.get(), roleOpt.get());
    }

    /**
     * Valida uma string de nome de role e a retorna como um Optional, sem lançar uma exceção.
     * A validação não diferencia maiúsculas de minúsculas.
     *
     * @param roleName A string com o nome da role.
     * @return Um {@code Optional<RoleEnum>} contendo o Enum se for válido, ou um Optional vazio caso contrário.
     */
    private Optional<RoleEnum> validateRoleName(String roleName) {
        if (roleName == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(RoleEnum.valueOf(roleName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Persiste as alterações na entidade User e a mapeia para um DTO de resposta.
     *
     * @param user A entidade User com o estado a ser salvo.
     * @return O {@link UserAccountDTO} representando o estado atualizado do usuário.
     */
    private UserAccountDTO saveAndMap(User user) {
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Record privado auxiliar para agrupar os resultados da validação de forma limpa e segura.
     *
     * @param user A entidade {@link User} validada.
     * @param role O {@link RoleEnum} validado.
     */
    private record ValidatedInput(User user, RoleEnum role) {}
}