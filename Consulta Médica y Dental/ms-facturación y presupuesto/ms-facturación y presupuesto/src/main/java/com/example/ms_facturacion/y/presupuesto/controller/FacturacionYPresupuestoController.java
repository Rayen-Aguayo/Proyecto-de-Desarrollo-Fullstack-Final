package com.example.ms_facturacion.y.presupuesto.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.ms_facturacion.y.presupuesto.dto.ApiResponse;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoDTO;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoResponse;
import com.example.ms_facturacion.y.presupuesto.service.FacturacionYPresupuestoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Facturación y Presupuesto", description = "Operaciones relacionadas con la gestión de cobros y presupuestos médicos")
@RestController
@RequestMapping("/api/v1/facturacio-y-presupuesto")
@RequiredArgsConstructor
public class FacturacionYPresupuestoController {

    private final FacturacionYPresupuestoService facturacionYPresupuestoService;

    @Operation(
        summary = "Listar facturaciones y presupuestos",
        description = "Retorna todos los registros de facturación del sistema. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<FacturacionYPresupuestoResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<FacturacionYPresupuestoResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(facturacionYPresupuestoService.listar(token))
                        .build()
        );
    }

    @Operation(
        summary = "Crear registro de facturación",
        description = "Crea un nuevo cobro o presupuesto. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "facturación y presupuesto creados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<FacturacionYPresupuestoResponse>> crear(
            @Valid @RequestBody FacturacionYPresupuestoDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<FacturacionYPresupuestoResponse>builder()
                        .success(true)
                        .message("Registro de facturación creado")
                        .data(facturacionYPresupuestoService.crear(dto, token))
                        .build()
        );
    }

    @Operation(
        summary = "Obtener facturación por ID",
        description = "Busca un cobro o presupuesto específico utilizando su identificador único. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "facturación y presupuesto obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<FacturacionYPresupuestoResponse>> obtener(
            @Parameter(description = "ID del registro de facturación", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        FacturacionYPresupuestoResponse FacturacionYPresupuesto = facturacionYPresupuestoService.obtener(id, token);

        EntityModel<FacturacionYPresupuestoResponse> recurso = EntityModel.of(FacturacionYPresupuesto);


        recurso.add(
                linkTo(methodOn(FacturacionYPresupuestoController.class).obtener(id, token))
                        .withSelfRel()
        );

        recurso.add(
                linkTo(methodOn(FacturacionYPresupuestoController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(FacturacionYPresupuestoController.class).actualizar(id, null, token))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(FacturacionYPresupuestoController.class).eliminar(id))
                        .withRel("delete")
        );

            return ResponseEntity.ok(
                    ApiResponse.<FacturacionYPresupuestoResponse>builder()
                            .success(true)
                            .data(facturacionYPresupuestoService.obtener(id, token))
                            .build()
            );
        
        }

    @Operation(
        summary = "Actualizar facturación por ID",
        description = "Modifica los datos de un registro de facturación existente. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "facturación y presupuestoa actualizados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<FacturacionYPresupuestoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacturacionYPresupuestoDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<FacturacionYPresupuestoResponse>builder()
                        .success(true)
                        .message("Registro de facturación actualizado")
                        .data(facturacionYPresupuestoService.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
        summary = "Eliminar facturación y presupuesto por ID",
        description = "Remueve permanentemente un registro del sistema. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Facturación y presupuestoa eliminados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        facturacionYPresupuestoService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Registro de facturación eliminado")
                        .build()
        );
    }
}