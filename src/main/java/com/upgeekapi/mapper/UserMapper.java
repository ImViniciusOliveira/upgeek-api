package com.upgeekapi.mapper;

import com.upgeekapi.dto.hateoas.AccountHateoasDTO;
import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Interface gerenciada pelo MapStruct para conversões relacionadas à entidade {@link User}.
 * <p>
 * Com a remoção dos dados de gamificação da entidade {@code User}, a responsabilidade
 * deste mapper foi refinada. Ele agora foca em duas tarefas principais:
 * <ol>
 *     <li>Converter os dados de <b>perfil</b> da entidade {@link User} para o {@link UserAccountDTO}.
 *     Os dados de gamificação (nível, xp) são intencionalmente ignorados, pois serão
 *     adicionados por outro serviço ou assembler.</li>
 *     <li>Aplicar atualizações parciais de um {@link UpdateAccountRequestDTO} para a entidade {@link User}.</li>
 * </ol>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    // --- Mapeamentos para DTOs de Resposta ---
    UserAccountDTO toDto(User user);

    /**
     * Mapeia uma lista de entidades {@link User} para uma lista de {@link UserAccountDTO}.
     * Aplica a mesma lógica de {@link #toDto(User)} para cada item.
     */
    List<UserAccountDTO> toDto(List<User> users);

    /**
     * Converte a entidade {@link User} para o seu modelo de representação HATEOAS {@link AccountHateoasDTO}.
     * <p>
     * Este método utiliza {@link #toDto(User)} para preencher os dados de perfil e os
     * envolve no DTO HATEOAS.
     *
     * @param user A entidade de domínio a ser convertida.
     * @return O DTO de resposta HATEOAS, pronto para receber os links e os dados de gamificação.
     */
    default AccountHateoasDTO toHateoasDTO(User user) {
        if (user == null) {
            return null;
        }
        // Cria o DTO de dados com as informações de perfil.
        UserAccountDTO userData = toDto(user);
        // Envolve no DTO HATEOAS.
        return new AccountHateoasDTO(userData);
    }

    // --- Mapeamento de Requisição (RequestDTO) para Entidade ---

    /**
     * Atualiza uma entidade {@link User} a partir de um {@link UpdateAccountRequestDTO}, ignorando campos nulos.
     * <p>
     * Esta estratégia permite que o cliente envie apenas os campos que deseja alterar (estilo PATCH).
     * Campos de sistema como ID, CPF, senha e papéis são explicitamente ignorados para segurança.
     *
     * @param dto O DTO com os dados de origem para a atualização.
     * @param user A entidade de destino que será atualizada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromDto(UpdateAccountRequestDTO dto, @MappingTarget User user);
}