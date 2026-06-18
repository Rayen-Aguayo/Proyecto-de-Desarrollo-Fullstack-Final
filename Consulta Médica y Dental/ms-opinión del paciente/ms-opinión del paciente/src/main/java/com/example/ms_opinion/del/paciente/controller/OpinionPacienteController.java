package com.example.ms_opinion.del.paciente.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_opinion.del.paciente.dto.ApiResponse;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
import com.example.ms_opinion.del.paciente.service.OpinionPacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Opinión del Paciente", description = "Operaciones relacionadas con las opiniones y valoraciones de los pacientes sobre su experiencia en la consulta médica y dental")
@RestController
@RequestMapping("/api/v1/opiniones")
@RequiredArgsConstructor
public class OpinionPacienteController {
    private final OpinionPacienteService opinionPacienteService;
    
    @Operation(
        summary = "Crear una nueva opinión del paciente",
        description = "Permite registrar una nueva opinión del paciente sobre su experiencia en la consulta médica y dental. Requiere rol USER o ADMIN.")
        
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Opinión creada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OpinionPacienteResponse>> crear(@Valid @RequestBody OpinionPacienteDTO dto,@RequestHeader("Authorization") String token) {
        OpinionPacienteResponse response = opinionPacienteService.crear(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<OpinionPacienteResponse>builder()
                .success(true)
                .message("Opinión registrada exitosamente")
                .data(response)
                .build()
        );
    }
    @Operation(
    summary = "Listar las opiniones de los pacientes",
    description = "Retorna todas las opiniones de los pacientes. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OpinionPacienteResponse>>> listar(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
            ApiResponse.<List<OpinionPacienteResponse>>builder()
                .success(true)
                .message("Listado de opiniones obtenido")
                .data(opinionPacienteService.listar(token))
                .build()
        );
    }
        @Operation(
        summary = "Obtener opinión por ID",
        description = "Busca una opinión específica utilizando su identificador único. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Opinión obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OpinionPacienteResponse>> obtener(
        
            @Parameter(description = "ID de la opinión del paciente", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        OpinionPacienteResponse opinion = opinionPacienteService.obtener(id, token);

        EntityModel<OpinionPacienteResponse> recurso = EntityModel.of(opinion);


           recurso.add(
            linkTo(methodOn(OpinionPacienteController.class).obtener(id, token))
                    .withSelfRel()
        );

        recurso.add(
                linkTo(methodOn(OpinionPacienteController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(OpinionPacienteController.class).eliminar(id))
                        .withRel("delete")
        );


        return ResponseEntity.ok(
            ApiResponse.<OpinionPacienteResponse>builder()
                .success(true)
                .message("Detalle de la opinión obtenido")
                .data(opinionPacienteService.obtener(id,token))
                .build()
        );
    }

    @Operation(
        summary = "Eliminar opinión por ID",
        description = "elimina una opinión específica utilizando su identificador único. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Opinión eliminada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        opinionPacienteService.eliminar(id);
        return ResponseEntity.status(200).body(
            ApiResponse.<Void>builder()
                .success(true)
                .message("La opinión ha sido eliminada")
                .build()
        );
    }
}