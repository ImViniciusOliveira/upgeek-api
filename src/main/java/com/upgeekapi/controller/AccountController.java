package com.upgeekapi.controller;

import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List; // Importar a classe List

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    /*
     * TODO: O MÉTODO ABAIXO É O ENDPOINT FINAL. Descomente-o quando a segurança for implementada.
     */
    /*
    @GetMapping("/me")
    @Operation(summary = "Obter dados da conta do usuário logado")
    public ResponseEntity<UserAccountDTO> getCurrentUserAccount(
            @AuthenticationPrincipal AuthPrincipal principal) {

        // A lógica do serviço buscaria por principal.email() ou algo similar
        UserAccountDTO userAccount = userService.findUserAccountByEmail(principal.email());
        return ResponseEntity.ok(userAccount);
    }
    */

    /**
     * Endpoint de teste para buscar uma conta de usuário por email.
     * Exemplo de chamada: GET http://localhost:8080/api/account/test/geek.master@email.com
     */
    @GetMapping("/test/{email}")
    @Operation(summary = "Obter dados da conta por email (PARA TESTE)",
            description = "Endpoint temporário para testar a busca de dados da conta sem um token válido.")
    public ResponseEntity<UserAccountDTO> getTestUserAccount(@PathVariable String email) {
        // Decodifica o email caso ele venha com caracteres especiais da URL
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        UserAccountDTO userAccount = userService.findUserAccountByEmail(decodedEmail);
        return ResponseEntity.ok(userAccount);
    }

    /**
     * Novo endpoint para buscar todos os usuários.
     */
    @GetMapping("/all")
    @Operation(summary = "Obter todos os usuários (PARA ADMINS/DEBUG)",
            description = "Retorna uma lista de todas as contas de usuário no sistema. ATENÇÃO: Use com cuidado. Em um ambiente de produção, este endpoint deve ser protegido para administradores e deve usar paginação.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    public ResponseEntity<List<UserAccountDTO>> getAllUsers() {
        List<UserAccountDTO> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }
}