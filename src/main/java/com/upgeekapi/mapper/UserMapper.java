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
 * Interface gerenciada pelo MapStruct para converter a entidade {@link User}
 * em seus DTOs de representação e para aplicar atualizações parciais.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    // --- Mapeamentos para DTOs de Resposta ---

    /**
     * Mapeia um User para um UserAccountDTO (DTO de dados puros).
     * Esta é a base para outras conversões.
     */
    @Mapping(source = "gamificationLevel", target = "level")
    @Mapping(source = "experiencePoints", target = "xp")
    @Mapping(target = "title", expression = "java(\"Colecionador Nível \" + user.getGamificationLevel())")
    UserAccountDTO toDto(User user);

    /**
     * Mapeia uma lista de Users para uma lista de UserAccountDTOs.
     */
    List<UserAccountDTO> toDto(List<User> users);

    /**
     * Converte a entidade {@link User} para o seu modelo de representação HATEOAS {@link AccountHateoasDTO}.
     * <p>
     * Esta implementação explícita com 'default' method resolve a ambiguidade do MapStruct
     * com DTOs imutáveis que possuem um "invólucro" (wrapper).
     *
     * @param user A entidade de domínio a ser convertida.
     * @return O DTO de resposta HATEOAS, com os dados preenchidos, pronto para receber os links.
     */
    default AccountHateoasDTO toHateoasDTO(User user) {
        if (user == null) {
            return null;
        }
        UserAccountDTO userData = toDto(user);
        return new AccountHateoasDTO(userData);
    }

    // --- Mapeamento de Requisição (RequestDTO) para Entidade ---

    /**
     * Atualiza uma entidade User a partir de um DTO, ignorando campos nulos.
     * Permite que o usuário atualize apenas os dados que fornecer na requisição (estilo PATCH).
     *
     * @param dto O DTO com os dados de origem para a atualização.
     * @param user A entidade de destino que será atualizada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "experiencePoints", ignore = true)
    @Mapping(target = "gamificationLevel", ignore = true)
    void updateUserFromDto(UpdateAccountRequestDTO dto, @MappingTarget User user);
}