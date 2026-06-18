package com.example.ms_medico.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_medico.dto.ApiResponse;
import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.service.MedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Medicos", description = "Operaciones relacionadas con Medicos")
@RestController
@RequestMapping("api/v1/medicos")
@RequiredArgsConstructor
public class MedicoController {
    private final MedicoService medicoService;

    @Operation(
        summary = "Crear Medicos",
        description = "Registrar un nuevo Medico en el sistema. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Medico creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado (Se requiere rol ADMIN)")
    })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medico>> crear(@Valid @RequestBody MedicoDTO dto) {

        Medico medico = medicoService.crear(dto);
         return ResponseEntity.status(201).body(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico creado")
                        .data(medico)
                        .build()
        );
    }

    @Operation(
        summary = "Listar Medicos",
        description = "Retorna todos los autores registrados. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Medico>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<Medico>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(medicoService.listar())
                        .build()
        );
    }

    @Operation(
        summary = "Listar Medicos",
        description = "Retorna todos los autores registrados. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "medico/a obtenido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    public ResponseEntity<ApiResponse<Medico>> obtener(
        @Parameter(description = "Identificador único del médico (RUN)", example = "12345678-9")
        @PathVariable String id,           
        @RequestHeader("Authorization") String token) {

        Medico medico = medicoService.obtener(id);

        EntityModel<Medico> recurso = EntityModel.of(medico);

        recurso.add(
                linkTo(methodOn(MedicoController.class).listar())
                        .withRel("all"));

        recurso.add(
                linkTo(methodOn(MedicoController.class).actualizar(id, null))
                        .withRel("update"));
        recurso.add(
                linkTo(methodOn(MedicoController.class).crear(null))
                        .withRel("create"));
        
        recurso.add(
                linkTo(methodOn(MedicoController.class).eliminar(id))
                        .withRel("delete"));

        return ResponseEntity.ok(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico obtenido")
                        .data(medicoService.obtener(id))
                        .build()
        );
    }

    @Operation(
            summary = "Actualizar Medico por Run",
            description = "Modifica los datos de un Medico existente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Medico/a actualizada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Medico/a no encontrada")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medico>> actualizar(
        @Parameter(description = "Identificador único del médico (RUN)", example = "12345678-9")
        @PathVariable String id,           
        @RequestHeader("Authorization") MedicoDTO dto){

        Medico medico = medicoService.actualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico actualizado")
                        .data(medico)
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar Medico por Run",
            description = "Elimina o deja sin efecto un/a Medico del sistema. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Medico/a eliminado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Medico/a no encontrada")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador único de la ficha médica", example = "1")
            @PathVariable String id) {

        medicoService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Medico eliminado")
                        .build()
        );
    }
}

