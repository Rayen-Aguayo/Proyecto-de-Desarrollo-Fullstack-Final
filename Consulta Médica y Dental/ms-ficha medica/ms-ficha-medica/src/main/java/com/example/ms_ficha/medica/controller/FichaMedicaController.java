package com.example.ms_ficha.medica.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_ficha.medica.dto.ApiResponse;
import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.service.FichaMedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Fichas Medicas", description = "Operaciones relacionadas con Fichas Medicas")
@RestController
@RequestMapping("/api/v1/fichas_medicas")
@RequiredArgsConstructor
public class FichaMedicaController {
    private final FichaMedicaService fichaMedicaService;

    @Operation(
            summary = "Crear ficha médica",
            description = "Registra una nueva ficha médica en el sistema. Requiere rol ADMIN y validación de Token JWT."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ficha médica creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado (Se requiere rol ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> crear(
            @Valid @RequestBody FichaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .message("Ficha médica creada")
                        .data(fichaMedicaService.crear(dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Listar fichas médicas",
            description = "Retorna un listado de todas las fichas médicas registradas. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado (Se requiere rol ADMIN)")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<FichaMedicaResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<FichaMedicaResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(fichaMedicaService.listar(token))
                        .build()
        );
    }

    @Operation(
            summary = "Obtener ficha médica por ID",
            description = "Busca y retorna los detalles de una ficha médica mediante su identificador único. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ficha médica encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ficha médica no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> obtener(
            @Parameter(description = "Identificador único de la ficha médica", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        FichaMedicaResponse fichaMedica = fichaMedicaService.obtener(id, token);

        return ResponseEntity.ok(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .message("Ficha médica encontrada")
                        .data(fichaMedica)
                        .build()
        );
    }

    @Operation(
            summary = "Actualizar ficha médica por ID",
            description = "Modifica los datos de una ficha médica existente. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ficha médica actualizada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ficha médica no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> actualizar(
            @Parameter(description = "Identificador único de la ficha médica", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody FichaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .message("Se actualizó la ficha médica")
                        .data(fichaMedicaService.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar ficha médica por ID",
            description = "Elimina una ficha médica del sistema. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ficha médica eliminada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ficha médica no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador único de la ficha médica", example = "1")
            @PathVariable Long id) {

        fichaMedicaService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se eliminó la ficha médica")
                        .build()
        );
    }
}