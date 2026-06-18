package com.example.ms_receta.medica.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_receta.medica.dto.ApiResponse;
import com.example.ms_receta.medica.dto.RecetaMedicaDTO;
import com.example.ms_receta.medica.dto.RecetaMedicaResponce;
import com.example.ms_receta.medica.service.RecetaMedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Receta Médica", description = "Operaciones relacionadas con la gestión de recetas médicas")
@RestController
@RequestMapping("/api/v1/recetasMedicas")
@RequiredArgsConstructor
public class RecetaMedicaController {

    private final RecetaMedicaService recetamedicaService;

    @Operation(
    summary = "Crear receta médica",
    description = "Crea una nueva receta médica. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Receta creada correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })  

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> crear(
            @Valid @RequestBody RecetaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .message("Receta médica creada")
                        .data(recetamedicaService.crear(dto, token))
                        .build()
        );
    }

        @Operation(
            summary = "Listar recetas médicas",
            description = "Retorna todas las recetas médicas del sistema. Requiere rol ADMIN."
        )
        @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RecetaMedicaResponce>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<RecetaMedicaResponce>>builder()
                        .success(true)
                        .data(recetamedicaService.listar(token))
                        .build()
        );
    }
    @Operation(
        summary = "Obtener receta médica por ID",
        description = "Busca una receta médica específica utilizando su identificador único. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Receta obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> obtener(

            @Parameter(description = "ID de la receta médica", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        RecetaMedicaResponce RecetaMedicaResponce = recetamedicaService.obtener(id, token); 


        EntityModel<RecetaMedicaResponce> recurso = EntityModel.of(RecetaMedicaResponce);


        recurso.add(
                linkTo(methodOn(RecetaMedicaController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(RecetaMedicaController.class).actualizar(id, null, token))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(RecetaMedicaController.class).eliminar(id))
                        .withRel("delete")
        );


        return ResponseEntity.ok(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .data(recetamedicaService.obtener(id, token))
                        .build()
        );
    }

    @Operation(
        summary = "Actualizar la receta médica por su ID",
        description = "Actualiza la información de una receta médica existente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Receta médica actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Receta médica no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .message("Receta médica actualizada")
                        .data(recetamedicaService.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
    summary = "Eliminar receta médica por su ID",
    description = "Busca una receta médica usando su identificador (ID) y la elimina. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Receta médica eliminada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Receta médica no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        recetamedicaService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Receta médica eliminada")
                        .build()
        );
    }
}
