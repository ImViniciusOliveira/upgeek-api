package com.upgeekapi.controller;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.dto.response.LoginDTO;
import com.upgeekapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller que gerencia os endpoints de autenticação e registro.
 * <p>
 * Atua como a porta de entrada para usuários na API, orquestrando o fluxo de
 * criação de novas contas e a autenticação para obter tokens de acesso.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para registro, login e verificação de token")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registra um novo usuário na plataforma.
     *
     * @param registrationRequest DTO contendo os dados validados para o novo registro.
     * @return Uma resposta HTTP 201 (Created) sem corpo em caso de sucesso.
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário", description = "Cria uma nova conta de usuário na plataforma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de registro inválidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados. O email ou CPF fornecido já está em uso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {
        authService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Autentica um usuário e retorna um token de acesso.
     *
     * @param loginRequest DTO contendo as credenciais (email e senha) do usuário.
     * @return Uma resposta HTTP 200 (OK) com um {@link LoginDTO} contendo o token JWT no corpo.
     */
    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário e gerar token JWT", description = "Recebe email e senha e, se as credenciais forem válidas, retorna um token de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Credenciais (email ou senha) inválidas.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna os dados do principal do usuário atualmente autenticado.
     * <p>
     * Este é um endpoint protegido usado para verificar a validade de um token
     * e obter as informações básicas (ID e roles) associadas a ele.
     *
     * @param principal O objeto {@link AuthPrincipal} injetado pelo Spring Security, representando o usuário logado.
     * @return Uma resposta HTTP 200 (OK) com os dados do principal no corpo.
     */
    @GetMapping("/me")
    @Operation(summary = "Verificar dados do usuário autenticado", description = "Endpoint protegido que retorna os dados do principal contidos no token JWT.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do principal retornados com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado. O token está ausente, é inválido ou expirou.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<AuthPrincipal> getAuthenticatedUser(@AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(principal);
    }
}