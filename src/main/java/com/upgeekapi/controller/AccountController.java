package com.upgeekapi.controller;

import com.upgeekapi.controller.assembler.AccountHateoasAssembler;
import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.hateoas.AccountHateoasDTO;
import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.entity.User;
import com.upgeekapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelo recurso da conta do usuário autenticado.
 * Segue os princípios HATEOAS para fornecer links para ações disponíveis.
 */
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account", description = "Endpoints para gerenciamento da conta do usuário autenticado")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AccountHateoasAssembler assembler;

    @GetMapping
    @Operation(summary = "Obter dados da minha conta",
            description = "Retorna as informações da conta do usuário atualmente autenticado, com links HATEOAS para outras ações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados da conta retornados com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token ausente, inválido ou expirado."),
            @ApiResponse(responseCode = "404", description = "Usuário associado ao token não encontrado no banco de dados.")
    })
    public AccountHateoasDTO getAccount(@AuthenticationPrincipal AuthPrincipal principal) {
        User user = userService.findUserById(principal.userId());
        return assembler.toModel(user);
    }

    @PutMapping
    @Operation(summary = "Atualizar dados da minha conta",
            description = "Permite que o usuário autenticado atualize seus próprios dados. Retorna a representação atualizada do recurso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "409", description = "Conflito de dados, como email ou username já em uso.")
    })
    public AccountHateoasDTO updateAccount(
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody UpdateAccountRequestDTO updateRequest) {

        User updatedUser = userService.updateUserAccount(principal.userId(), updateRequest);
        return assembler.toModel(updatedUser);
    }

    @DeleteMapping
    @Operation(summary = "Deletar minha conta",
            description = "Permite que o usuário autenticado delete permanentemente sua própria conta. Esta ação é irreversível.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")
    })
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal AuthPrincipal principal) {
        userService.deleteUserById(principal.userId());
        return ResponseEntity.noContent().build();
    }
}