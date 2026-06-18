package com.example.ms_paciente.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_paciente.service.PacienteService;
import com.example.ms_paciente.dto.PacienteDTO;
import com.example.ms_paciente.model.Paciente;
import com.example.ms_paciente.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@Tag(name = "Pacientes", description = "Operaciones relacionadas con pacientes")
@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @Operation(summary = "Creacion de pacientes", description = "Permite crear un nuevo paciente. Solo accesible para ADMIN.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Paciente>> crear(@Valid @RequestBody PacienteDTO dto) {
        Paciente paciente = pacienteService.crear(dto);
        return ResponseEntity.status(201).body(
                ApiResponse.<Paciente>builder()
                        .success(true)
                        .message("Paciente creado")
                        .data(paciente)
                        .build()
        );
    }

    @Operation(summary = "Listar pacientes", description = "Obtiene todos los pacientes. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Paciente>>> listar() {
        return ResponseEntity.ok(
                ApiResponse.<List<Paciente>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(pacienteService.listar())
                        .build()
        );
    }

    @Operation(summary = "Obtener paciente por su run", description = "Busca un paciente por su RUN. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paciente obtenido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{run}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // ✅ Fix: otros microservicios con rol USER también necesitan consultar pacientes
    public ResponseEntity<ApiResponse<EntityModel<Paciente>>> obtener(
            @Parameter(description = "RUN del paciente", example = "11111111-1")
            @PathVariable String run) {

        Paciente paciente = pacienteService.obtener(run);

        EntityModel<Paciente> recurso = EntityModel.of(paciente);
        recurso.add(linkTo(methodOn(PacienteController.class).listar()).withRel("all"));
        recurso.add(linkTo(methodOn(PacienteController.class).actualizar(run, null)).withRel("update"));
        recurso.add(linkTo(methodOn(PacienteController.class).crear(null)).withRel("create"));
        recurso.add(linkTo(methodOn(PacienteController.class).eliminar(run)).withRel("delete"));

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<Paciente>>builder()
                        .success(true)
                        .message("Paciente obtenido")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Actualizar paciente por su run", description = "Actualiza un paciente existente. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paciente actualizado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{run}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Paciente>> actualizar(
        @Parameter(description = "RUN del paciente", example = "11111111-1")
        @PathVariable String run,
        @Valid @RequestBody PacienteDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.<Paciente>builder()
                        .success(true)
                        .message("Paciente actualizado")
                        .data(pacienteService.actualizar(run, dto))
                        .build()
        );
    }

    @Operation(summary = "Eliminar paciente por su run", description = "Elimina un paciente por su RUN. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paciente eliminado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{run}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(
        @Parameter(description = "RUN del paciente", example = "11111111-1")
        @PathVariable String run) {

        pacienteService.eliminar(run);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Paciente eliminado")
                        .build()
        );
    }
}