package com.example.ms_registro.de.materiales.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_registro.de.materiales.dto.ApiResponse;
import com.example.ms_registro.de.materiales.dto.RegistroMaterialesDTO;
import com.example.ms_registro.de.materiales.model.RegistroMateriales;
import com.example.ms_registro.de.materiales.service.RegistroMaterialesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Registro de Materiales", description = "Operaciones relacionadas con el registro y gestión de materiales médicos")
@RestController
@RequestMapping("/api/v1/registrosMateriales")
@RequiredArgsConstructor
public class RegistroMaterialesController {

    private final RegistroMaterialesService registroMaterialesService;
    
    @Operation(
    summary = "Crear registro de materiales",
    description = "Crea un nuevo registro de materiales. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Registro creado correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })  

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> crear(@Valid @RequestBody RegistroMaterialesDTO dto) {

        RegistroMateriales registro = registroMaterialesService.crear(dto);

        return ResponseEntity.status(201).body(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro creado")
                        .data(registro)
                        .build()
        );
    }
        @Operation(
            summary = "Listar Registros de Materiales",
            description = "Retorna todos los registros de materiales del sistema. Requiere rol ADMIN."
        )
        @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RegistroMateriales>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<RegistroMateriales>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(registroMaterialesService.listar())
                        .build()
        );
    }
    @Operation(
        summary = "Obtener registro de materiales por ID",
        description = "Busca un registro específico utilizando su identificador único. Requiere rol ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro obtenido exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> obtener(
            @Parameter(description = "ID del registro de materiales", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        RegistroMateriales RegistroMateriales = registroMaterialesService.obtener(id); 


        EntityModel<RegistroMateriales> recurso = EntityModel.of(RegistroMateriales);


        recurso.add(
                linkTo(methodOn(RegistroMaterialesController.class).listar())
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(RegistroMaterialesController.class).actualizar(id, null))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(RegistroMaterialesController.class).eliminar(id))
                        .withRel("delete")
        );

        return ResponseEntity.ok(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro obtenido")
                        .data(registroMaterialesService.obtener(id))
                        .build()
        );
    }

    @Operation(
        summary = "Actualizar el registro de materiales por su ID",
        description = "Actualiza la información de un registro de materiales existente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody RegistroMaterialesDTO dto) {

        RegistroMateriales registro = registroMaterialesService.actualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro actualizado")
                        .data(registro)
                        .build()
        );
    }
    @Operation(
    summary = "Eliminar registro de materiales por su ID",
    description = "Busca un registro de materiales usando su identificador (ID) y lo elimina. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registro eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        registroMaterialesService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("registro eliminado")
                        .build()
        );
    }
}
