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
 * Constrói o modelo de representação HATEOAS para a entidade {@link User}.
 * <p>
 * Esta classe implementa o padrão "Assembler" do Spring HATEOAS. Sua única
 * responsabilidade é enriquecer o DTO de dados com links contextuais da API,
 * como links para o próprio recurso, atualização e exclusão.
 * <p>
 * A conversão dos dados da entidade para o DTO é delegada ao {@link UserMapper},
 * promovendo uma clara separação de responsabilidades.
 */
@Component
public class AccountHateoasAssembler extends RepresentationModelAssemblerSupport<User, AccountHateoasDTO> {

    private final UserMapper userMapper;

    /**
     * Construtor que injeta as dependências necessárias.
     *
     * @param userMapper O mapper responsável pela conversão de dados entre User e DTOs.
     */
    @Autowired // A anotação é opcional em construtores únicos, mas mantida para clareza.
    public AccountHateoasAssembler(UserMapper userMapper) {
        super(AccountController.class, AccountHateoasDTO.class);
        this.userMapper = userMapper;
    }

    /**
     * Converte uma entidade {@link User} em seu modelo de representação {@link AccountHateoasDTO},
     * enriquecido com links HATEOAS.
     * <p>
     * O processo ocorre em duas etapas:
     * <ol>
     *     <li>Os dados da entidade são mapeados para o DTO usando o {@link UserMapper}.</li>
     *     <li>Links relevantes (self, update, delete) são adicionados ao modelo.</li>
     * </ol>
     * <strong>Nota sobre {@code methodOn}:</strong> O uso de {@code null} como argumento
     * nos métodos do controller é uma técnica padrão do Spring HATEOAS. O framework
     * intercepta a chamada para construir o link, e o código do método do controller
     * nunca é de fato executado com valores nulos.
     *
     * @param user a entidade de usuário a ser convertida (não deve ser {@literal nula}).
     * @return o modelo de representação HATEOAS (nunca {@literal nulo}).
     */
    @Override
    @NonNull
    // Suprime avisos da IDE sobre o uso de 'null' em methodOn, que é o comportamento esperado.
    @SuppressWarnings("ConstantConditions")
    public AccountHateoasDTO toModel(@NonNull User user) {
        // 1. Delega a conversão de dados para o mapper, separando responsabilidades.
        AccountHateoasDTO model = userMapper.toHateoasDTO(user);

        // 2. Adiciona os links HATEOAS que definem as ações possíveis sobre o recurso.
        model.add(linkTo(methodOn(AccountController.class).getAccount(null)).withSelfRel());
        model.add(linkTo(methodOn(AccountController.class).updateAccount(null, null)).withRel("update"));
        model.add(linkTo(methodOn(AccountController.class).deleteAccount(null)).withRel("delete"));

        return model;
    }
}