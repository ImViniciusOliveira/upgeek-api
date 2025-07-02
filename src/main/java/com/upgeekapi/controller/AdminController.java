package com.upgeekapi.controller;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para endpoints de administração, protegidos para acesso apenas por usuários com a role 'ADMIN'.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Endpoints de Administração")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/users/{userId}/roles/{roleName}")
    @Operation(summary = "Adicionar uma role a um usuário (Admin)",
            description = "Permite que um administrador adicione uma permissão (role) a um usuário específico.")
    public ResponseEntity<UserAccountDTO> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        UserAccountDTO updatedUser = adminService.addRoleToUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{userId}/roles/{roleName}")
    @Operation(summary = "Remover uma role de um usuário (Admin)",
            description = "Permite que um administrador remova uma permissão (role) de um usuário específico.")
    public ResponseEntity<UserAccountDTO> removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
        UserAccountDTO updatedUser = adminService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }
}