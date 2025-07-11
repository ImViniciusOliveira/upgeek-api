package com.upgeekapi.controller;

import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.entity.RoleEnum;
import com.upgeekapi.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para endpoints de administração, protegidos para acesso exclusivo de usuários com a permissão 'ADMIN'.
 * <p>
 * Delega as operações de gerenciamento de permissões para o {@link AdminService}.
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Endpoints de Administração")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Adiciona uma permissão (role) a um usuário específico.
     *
     * @param userId O ID do usuário a ser modificado.
     * @param role   A permissão a ser adicionada (ex: ROLE_ADMIN, ROLE_USER). Spring converte a string do path para o enum.
     * @return Um {@link UserAccountDTO} com os dados do usuário atualizado.
     */
    @PostMapping("/users/{userId}/roles/{role}")
    @Operation(summary = "Adicionar uma role a um usuário (Admin)",
            description = "Permite que um administrador adicione uma permissão (role) a um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão adicionada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: role inválida ou usuário já possui a role).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O usuário autenticado não é um administrador.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<UserAccountDTO> addRoleToUser(@PathVariable Long userId, @PathVariable RoleEnum role) {
        UserAccountDTO updatedUser = adminService.addRoleToUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Remove uma permissão (role) de um usuário específico.
     *
     * @param userId O ID do usuário a ser modificado.
     * @param role   A permissão a ser removida (ex: ROLE_ADMIN, ROLE_USER). Spring converte a string do path para o enum.
     * @return Um {@link UserAccountDTO} com os dados do usuário atualizado.
     */
    @DeleteMapping("/users/{userId}/roles/{role}") // CORREÇÃO: O nome da variável no path foi alinhado para consistência.
    @Operation(summary = "Remover uma role de um usuário (Admin)",
            description = "Permite que um administrador remova uma permissão (role) de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão removida com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário não possui a role ou é a última permissão do usuário).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O usuário autenticado não é um administrador.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<UserAccountDTO> removeRoleFromUser(@PathVariable Long userId, @PathVariable RoleEnum role) {
        UserAccountDTO updatedUser = adminService.removeRoleFromUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }
}