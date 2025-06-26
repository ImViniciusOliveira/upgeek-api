package com.upgeekapi.controller;

import com.upgeekapi.dto.request.CreateUserRequestDTO;
import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.dto.response.UserAccountDTO;
import com.upgeekapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controller responsável pelos recursos da conta do usuário.
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "Endpoints para gerenciamento de contas de usuário")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Obter todos os usuários",
            description = "Retorna uma lista de todas as contas de usuário no sistema. ATENÇÃO: Em produção, este endpoint deve ser protegido para administradores e deve usar paginação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    public ResponseEntity<List<UserAccountDTO>> getAllUsers() {
        List<UserAccountDTO> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/test/{email}")
    @Operation(
            summary = "Obter dados da conta por email (Teste)",
            description = "Endpoint temporário para testar a busca de dados de uma conta específica sem a necessidade de um token de autenticação válido."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados da conta retornados com sucesso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado com o email fornecido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<UserAccountDTO> getTestUserAccount(@PathVariable String email) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        UserAccountDTO userAccount = userService.findUserAccountByEmail(decodedEmail);
        return ResponseEntity.ok(userAccount);
    }

    /*
     * TODO: O endpoint de criação abaixo é para fins de TESTE e DEBUG.
     * Em um ambiente de produção, a criação de usuário (registro) deve fazer parte
     * de um `AuthController`, envolver hashing de senha e lógica de confirmação de email.
     */
    @PostMapping("/test/create")
    @Operation(summary = "Criar um novo usuário (Teste)",
            description = "Registra um novo usuário na plataforma. Este endpoint é para fins de teste e pode ser removido ou protegido no futuro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados, o email fornecido já existe",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<UserAccountDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        UserAccountDTO createdUser = userService.createUser(createUserRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /*
     * TODO: O endpoint de deleção abaixo é para fins de TESTE e DEBUG.
     * Em um ambiente de produção, a deleção de usuário deve ser uma operação protegida
     * (acessível apenas por administradores ou pelo próprio usuário com confirmação),
     * e provavelmente usaria uma estratégia de "soft delete".
     */
    @DeleteMapping("/test/delete/{email}")
    @Operation(summary = "Deletar uma conta de usuário (Teste)",
            description = "Remove uma conta de usuário. Este endpoint é para fins de teste e deve ser removido ou protegido no futuro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o email fornecido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        userService.deleteUserByEmail(decodedEmail);
        return ResponseEntity.noContent().build();
    }

    /*
     * TODO: O endpoint /me abaixo deve ser ativado quando a segurança for implementada.
     * Ele será o método principal para um usuário obter seus próprios dados.
     * A documentação deverá incluir respostas para os status 401 (Unauthorized) e 403 (Forbidden).
     */
    /*
    @GetMapping("/me")
    @Operation(summary = "Obter dados da conta do usuário logado")
    public ResponseEntity<UserAccountDTO> getCurrentUserAccount(
            @AuthenticationPrincipal AuthPrincipal principal) {

        UserAccountDTO userAccount = userService.findUserAccountByEmail(principal.getEmail());
        return ResponseEntity.ok(userAccount);
    }
    */
}