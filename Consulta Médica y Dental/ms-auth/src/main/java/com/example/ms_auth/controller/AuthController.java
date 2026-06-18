package com.example.ms_auth.controller;

import com.example.ms_auth.dto.*;
import com.example.ms_auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "Operaciones de registro, login y renovación de token")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "Registrar usuario",
            description = "Crea un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida o el usuario ya existe")
    })
    
    @PostMapping("/register")
    public ResponseEntity<com.example.ms_auth.dto.ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req) {
        log.info("POST /auth/register - usuario: {}", req.getUsername());

        AuthResponse res = service.register(req);

        return ResponseEntity.ok(
                com.example.ms_auth.dto.ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Usuario registrado")
                        .data(res)
                        .build()
        );
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Valida credenciales y retorna accessToken y refreshToken"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })

    @PostMapping("/login")
    public ResponseEntity<com.example.ms_auth.dto.ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req) {

        log.info("POST /auth/login - usuario: {}", req.getUsername());
        AuthResponse res = service.login(req);
        
        
        return ResponseEntity.ok(
                com.example.ms_auth.dto.ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login exitoso")
                        .data(res)
                        .build()
        );
    }

    @Operation(
            summary = "Renovar token",
            description = "Genera un nuevo access token utilizando un refresh token válido"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token renovado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })

    @PostMapping("/refresh")
    public ResponseEntity<com.example.ms_auth.dto.ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest req) {
        log.info("POST /auth/refresh - procesando renovación");
        AuthResponse res = service.refresh(req.getRefreshToken());

        return ResponseEntity.ok(
                com.example.ms_auth.dto.ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Token renovado")
                        .data(res)
                        .build()
        );
    }
}