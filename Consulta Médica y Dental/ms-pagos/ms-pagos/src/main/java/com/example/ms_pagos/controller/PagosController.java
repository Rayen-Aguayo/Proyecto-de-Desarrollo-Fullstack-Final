package com.example.ms_pagos.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pagos.dto.ApiResponse;
import com.example.ms_pagos.dto.PagosDTO;
import com.example.ms_pagos.dto.PagosResponse;
import com.example.ms_pagos.service.PagosService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Pagos", description = "Operaciones relacionadas con la gestión de pagos")
@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagosController {

    private final PagosService pagosService;

    @Operation(
    summary = "Crear pago",
    description = "Crea un nuevo pago. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })  
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> crear(
            @Valid @RequestBody PagosDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<PagosResponse>builder()
                        .success(true)
                        .message("Pago registrado")
                        .data(pagosService.crear(dto, token))
                        .build()
        );
    }

        @Operation(
            summary = "Listar pagos",
            description = "Retorna todos los pagos del sistema. Requiere rol ADMIN."
        )
        @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PagosResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<PagosResponse>>builder()
                        .success(true)
                        .data(pagosService.listar(token))
                        .build()
        );
    }
    
    @Operation(
        summary = "Obtener pago por ID",
        description = "Busca un pago específico utilizando su identificador único. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> obtener(
            @Parameter(description = "ID del pago", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        PagosResponse pagosResponse = pagosService.obtener(id, token);


        EntityModel<PagosResponse> recurso = EntityModel.of(pagosResponse);


        recurso.add(
                linkTo(methodOn(PagosController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(PagosController.class).actualizar(id, null, token))
                        .withRel("update")
        );



        return ResponseEntity.ok(
                ApiResponse.<PagosResponse>builder()
                        .success(true)
                        .data(pagosService.obtener(id, token))
                        .build()
        );
    }
    @Operation(
        summary = "Actualizar el pago por su ID",
        description = "Actualiza la información de un pago existente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pago actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagosDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<PagosResponse>builder()
                        .success(true)
                        .message("Pago actualizado")
                        .data(pagosService.actualizar(id, dto, token))
                        .build()
        );
    }
}
