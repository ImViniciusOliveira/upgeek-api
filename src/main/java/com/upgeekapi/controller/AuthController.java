package com.upgeekapi.controller;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.response.LoginResponseDTO;
import com.upgeekapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação e geração de token")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário e gerar token JWT",
            description = "Recebe email e senha e, se as credenciais forem válidas, retorna um token de acesso.")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Verificar dados do usuário autenticado",
            description = "Endpoint protegido que retorna os dados do principal contidos no token JWT. Ótimo para testar se o token é válido.")
    public ResponseEntity<AuthPrincipal> getAuthenticatedUser(@AuthenticationPrincipal AuthPrincipal principal) {
        // Se a requisição chegar aqui, significa que o JwtAuthenticationFilter funcionou
        // e o Spring conseguiu injetar o AuthPrincipal.
        return ResponseEntity.ok(principal);
    }
}