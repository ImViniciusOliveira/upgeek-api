package com.upgeekapi.mapper;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.User;
import org.springframework.stereotype.Component;

/**
 * Implementação concreta do {@link Mapper} para converter a entidade {@link User}
 * em um {@link UserAccountDTO}.
 * A anotação @Component permite que o Spring gerencie esta classe como um Bean e a
 * injete em outros componentes, como serviços.
 */
@Component
public class UserMapper implements Mapper<User, UserAccountDTO> {

    @Override
    public UserAccountDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        String userTitle = "Colecionador Nível " + user.getGamificationLevel();

        return new UserAccountDTO(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getGamificationLevel(),
                user.getExperiencePoints(),
                userTitle
        );
    }
}