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
 * Este DTO atua como um "invólucro" (wrapper) para os dados do {@link UserAccountDTO},
 * adicionando a capacidade de carregar links de ações da API. A anotação {@code @JsonUnwrapped}
 * é uma decisão de design chave que "achata" a estrutura do JSON final, resultando em
 * uma resposta de API mais limpa e intuitiva para o consumidor.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserAccountResponse", description = "Representação da conta do usuário com links HATEOAS para ações disponíveis.")
public class AccountHateoasDTO extends RepresentationModel<AccountHateoasDTO> {

    /**
     * Contém os dados da conta do usuário. A anotação {@code @JsonUnwrapped}
     * "desempacota" este objeto no JSON final, promovendo seus campos
     * para o nível superior da resposta.
     * <p>
     * <b>Exemplo de Transformação:</b>
     * <br>
     * De: <code>{ "userData": { "id": 1, ... }, "_links": { ... } }</code>
     * <br>
     * Para: <code>{ "id": 1, ..., "_links": { ... } }</code>
     * <br>
     * Este design evita um nível de aninhamento desnecessário, tornando a API mais fácil de usar.
     */
    @JsonUnwrapped
    private UserAccountDTO userData;
}