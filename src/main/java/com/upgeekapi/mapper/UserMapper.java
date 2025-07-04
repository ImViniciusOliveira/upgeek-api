package com.upgeekapi.mapper;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface gerenciada pelo MapStruct para converter a entidade {@link User}
 * em um {@link UserAccountDTO}.
 * <p>
 * A implementação desta interface é gerada automaticamente em tempo de compilação,
 * eliminando a necessidade de código manual e garantindo performance e segurança.
 */
@Mapper(componentModel = "spring") // Diz ao MapStruct para gerar um Bean do Spring
public interface UserMapper {

    /**
     * Mapeia um User para um UserAccountDTO.
     * O MapStruct mapeia campos com o mesmo nome automaticamente (name, username, email).
     * Os campos com nomes diferentes ou com lógica customizada precisam de regras.
     *
     * @param user A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    @Mapping(source = "gamificationLevel", target = "level")
    @Mapping(source = "experiencePoints", target = "xp")
    @Mapping(target = "title", expression = "java(\"Colecionador Nível \" + user.getGamificationLevel())")
    UserAccountDTO toDto(User user);

    /**
     * Mapeia uma lista de Users para uma lista de UserAccountDTOs.
     * O MapStruct gera automaticamente o loop para nós.
     */
    List<UserAccountDTO> toDto(List<User> users);
}