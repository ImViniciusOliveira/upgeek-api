package com.upgeekapi.mapper;

import com.upgeekapi.dto.hateoas.AccountHateoasDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface gerenciada pelo MapStruct para converter a entidade {@link User}
 * em seus DTOs de representação.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

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

    // --- Mapeamento para o DTO HATEOAS ---

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

        // Passo 1: Usamos o método que o MapStruct já sabe implementar para converter os dados base.
        UserAccountDTO userData = toDto(user);

        // Passo 2: Chamamos manualmente o construtor do DTO "invólucro", que é imutável.
        return new AccountHateoasDTO(userData);
    }
}