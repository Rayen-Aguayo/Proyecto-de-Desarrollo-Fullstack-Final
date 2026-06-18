package com.example.ms_registro.de.atenciones.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_registro.de.atenciones.dto.ApiResponse;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesDTO;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesResponse;
import com.example.ms_registro.de.atenciones.service.RegistroAtencionesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Registro de Atenciones", description = "Operaciones relacionadas con la gestión de registros de atenciones médicas")
@RestController
@RequestMapping("/api/v1/registro-atenciones")
@RequiredArgsConstructor
public class RegistroAtencionesController {

    private final RegistroAtencionesService service;

    @Operation(
        summary = "Crear registro de atenciones",
        description = "Crea un nuevo registro de atención médica. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Registro de atenciones creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> crear(
            @Valid @RequestBody RegistroAtencionesDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .message("Registro de atenciones creado")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @Operation(
        summary = "Listar registros de atenciones",
        description = "Retorna todos los registros de atención médica del sistema. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<RegistroAtencionesResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<RegistroAtencionesResponse>>builder()
                        .success(true)
                        .data(service.listar(token))
                        .build()
        );
    }

    @Operation(
        summary = "Obtener registro de atenciones por ID",
        description = "Busca un registro de atenciones específico utilizando su identificador único. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro de atenciones obtenido exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> obtener(
            @Parameter(description = "ID del registro de atenciones", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        RegistroAtencionesResponse RegistroAtencionesResponse = service.obtener(id, token);

        EntityModel<RegistroAtencionesResponse> recurso = EntityModel.of(RegistroAtencionesResponse);


        recurso.add(
                linkTo(methodOn(RegistroAtencionesController.class).obtener(id, token))
                        .withSelfRel()
        );

        recurso.add(
                linkTo(methodOn(RegistroAtencionesController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(RegistroAtencionesController.class).actualizar(id, null, token))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(RegistroAtencionesController.class).eliminar(id))
                        .withRel("delete")
        );

        return ResponseEntity.ok(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .data(service.obtener(id, token))
                        .build()
        );
    }

    @Operation(
        summary = "Actualizar registro de atenciones por ID",
        description = "Modifica los datos de un registro de atenciones existente. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro de atenciones actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Registro no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RegistroAtencionesDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .message("Registro de atenciones actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
        summary = "Eliminar registro de atenciones por ID",
        description = "Remueve permanentemente un registro del sistema. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro de atenciones eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Registro de atenciones eliminado")
                        .build()
        );
    }
}
