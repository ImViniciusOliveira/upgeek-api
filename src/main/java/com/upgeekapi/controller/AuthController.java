package com.upgeekapi.controller;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.dto.response.LoginResponseDTO;
import com.upgeekapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller que gerencia os endpoints públicos de autenticação,
 * como registro e login de usuários.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação e geração de token")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário",
            description = "Cria uma nova conta de usuário na plataforma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Conflito de dados. O email, username ou CPF fornecido já está em uso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> register(@RequestBody RegistrationRequestDTO registrationRequest) {
        authService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário e gerar token JWT",
            description = "Recebe email e senha e, se as credenciais forem válidas, retorna um token de acesso.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação bem-sucedida, token JWT retornado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado. Credenciais (email ou senha) inválidas.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Verificar dados do usuário autenticado",
            description = "Endpoint protegido que retorna os dados do principal contidos no token JWT. Usado para verificar a validade de um token e obter os dados do usuário logado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do principal retornados com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado. O token está ausente, é inválido ou expirou.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<AuthPrincipal> getAuthenticatedUser(@AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(principal);
    }
}
