package com.upgeekapi.controller.assembler;

import com.upgeekapi.controller.AccountController;
import com.upgeekapi.dto.hateoas.AccountHateoasDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Montador que adiciona os links HATEOAS ao {@link AccountHateoasDTO}.
 * Delega a conversão de dados para o {@link UserMapper}, focando apenas
 * na lógica de construção de links.
 */
@Component
public class AccountHateoasAssembler extends RepresentationModelAssemblerSupport<User, AccountHateoasDTO> {

    private final UserMapper userMapper;

    @Autowired
    public AccountHateoasAssembler(UserMapper userMapper) {
        super(AccountController.class, AccountHateoasDTO.class);
        this.userMapper = userMapper;
    }

    /**
     * Converte a entidade {@link User} em sua representação HATEOAS.
     *
     * @param user a entidade de usuário (não deve ser {@literal nula}).
     * @return o modelo de representação HATEOAS (nunca {@literal nulo}).
     */
    @Override
    @NonNull // 1. CORREÇÃO: Honra o contrato da classe pai, que espera um retorno não-nulo.
    @SuppressWarnings("ConstantConditions") // 2. CORREÇÃO: Informa à IDE que os 'nulls' abaixo são intencionais e seguros.
    public AccountHateoasDTO toModel(@NonNull User user) { // 1. CORREÇÃO: Honra o contrato que espera um parâmetro não-nulo.
        // Delega o mapeamento de dados para o mapper dedicado.
        AccountHateoasDTO model = userMapper.toHateoasDTO(user);

        // Adiciona os links HATEOAS. Os 'nulls' são apenas marcadores de posição
        // para o construtor de links e o código do controller nunca é executado com eles.
        model.add(linkTo(methodOn(AccountController.class).getAccount(null)).withSelfRel());
        model.add(linkTo(methodOn(AccountController.class).updateAccount(null, null)).withRel("update"));
        model.add(linkTo(methodOn(AccountController.class).deleteAccount(null)).withRel("delete"));

        return model;
    }
}