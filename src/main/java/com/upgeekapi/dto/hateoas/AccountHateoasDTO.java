package com.upgeekapi.dto.hateoas;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.upgeekapi.dto.response.UserAccountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Representação HATEOAS de uma conta de usuário.
 * <p>
 * Este DTO é projetado para ser imutável, atuando como um "invólucro" (wrapper)
 * para os dados do {@link UserAccountDTO} e adicionando a capacidade de carregar
 * links de ações da API. A anotação {@code @JsonUnwrapped} garante que o JSON
 * final seja "plano" para uma resposta de API mais limpa.
 */
@Getter // Apenas Getters, sem Setters para promover a imutabilidade.
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserAccountResponse", description = "Representação da conta do usuário com links HATEOAS para ações disponíveis.")
public class AccountHateoasDTO extends RepresentationModel<AccountHateoasDTO> {

    /**
     * Contém os dados da conta do usuário. A anotação @JsonUnwrapped
     * "desembrulha" este objeto no JSON final, promovendo seus campos
     * para o nível superior.
     * <p>
     * Exemplo de transformação:
     * <br>
     * de: <code>{ "userData": { "id": 1, ... }, "_links": { ... } }</code>
     * <br>
     * para: <code>{ "id": 1, ..., "_links": { ... } }</code>
     */
    @JsonUnwrapped
    private UserAccountDTO userData;
}