package com.upgeekapi.dto.hateoas;

//import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.upgeekapi.dto.response.UserAccountDTO; // Importamos o DTO que já tem os dados
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Representação HATEOAS de uma conta de usuário.
 * <p>
 * Este DTO atua como um "invólucro" (wrapper) para os dados do {@link UserAccountDTO},
 * adicionando a capacidade de carregar links de ações da API (como 'update' e 'delete').
 * A anotação {@code @JsonUnwrapped} é uma técnica poderosa que garante que o JSON final
 * seja "plano", exibindo os campos de UserAccountDTO no nível raiz, ao lado dos links,
 * em vez de dentro de um objeto aninhado.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AccountHateoasDTO extends RepresentationModel<AccountHateoasDTO> {

    /**
     * Contém os dados da conta do usuário. A anotação @JsonUnwrapped
     * "desembrulha" este objeto no JSON final, promovendo seus campos
     * para o nível superior.
     * ----
     * de { "userData": { "id": 1, "username": "kain" ... }, "_links": { ... } },
     * para { "id": 1, "username": "kain" ..., "_links": { ... } }
     */
    // @JsonUnwrapped

    private UserAccountDTO userData;
}