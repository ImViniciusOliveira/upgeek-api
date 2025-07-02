package com.upgeekapi.controller;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.UpdateAccountRequestDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos recursos da conta do usuário autenticado.
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "Endpoints para gerenciamento da conta do usuário autenticado")
@SecurityRequirement(name = "bearerAuth") // Exige autenticação para TODOS os endpoints neste controller
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados da minha conta",
            description = "Retorna as informações da conta do usuário atualmente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados da conta retornados com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token ausente, inválido ou expirado.")
    })
    public ResponseEntity<UserAccountDTO> getMyAccount(@AuthenticationPrincipal AuthPrincipal principal) {
        UserAccountDTO userAccount = userService.findUserAccountById(principal.userId());
        return ResponseEntity.ok(userAccount);
    }

    @PutMapping("/me")
    @Operation(summary = "Atualizar dados da minha conta",
            description = "Permite que o usuário autenticado atualize seus próprios dados, como o nome de exibição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")
    })
    public ResponseEntity<UserAccountDTO> updateMyAccount(
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody UpdateAccountRequestDTO updateRequest) {

        UserAccountDTO updatedUser = userService.updateUser(principal.userId(), updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Deletar minha conta",
            description = "Permite que o usuário autenticado delete permanentemente sua própria conta. Esta ação é irreversível.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")
    })
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal AuthPrincipal principal) {
        userService.deleteUserById(principal.userId());
        return ResponseEntity.noContent().build();
    }

}