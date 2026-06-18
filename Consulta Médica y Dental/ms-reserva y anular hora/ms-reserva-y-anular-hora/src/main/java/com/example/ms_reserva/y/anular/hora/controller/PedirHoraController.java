package com.example.ms_reservar.y.anular.hora.controller;

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

import com.example.ms_reservar.y.anular.hora.dto.ApiResponse;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reservar.y.anular.hora.service.PedirHoraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Pedir hora", description = "Operaciones relacionadas con la gestión de reservas y anulaciones de horas médicas")
@RestController
@RequestMapping("/api/v1/reservar-y-anular-hora")
@RequiredArgsConstructor

public class PedirHoraController { 
    private final PedirHoraService pedirHoraService;

    @Operation(
        summary = "Pedir hora",
        description = "Crea una nueva reserva de hora médica. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Reserva de hora creada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> crear(
            @Valid @RequestBody PedirHoraDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .message("Se reservo la hora")
                        .data(pedirHoraService.crear(dto, token))
                        .build()
        );
    }

    @Operation(
        summary = "Listar reservas de hora",
        description = "Retorna todas las reservas de hora del sistema. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PedirHoraResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<PedirHoraResponse>>builder()
                        .success(true)
                        .data(pedirHoraService.listar(token))
                        .build()
        );
    }

    @Operation(
        summary = "Obtener hora medica por ID",
        description = "Buscar una hora de un paciente que haya tomado utilizando su identificador único. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Hora medica obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> obtener(
            @Parameter(description = "ID de la hora medica", example = "1")
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        
        PedirHoraResponse PedirHora = pedirHoraService.obtener(id, token);

        EntityModel<PedirHoraResponse> recurso = EntityModel.of(PedirHora);


        recurso.add(
                linkTo(methodOn(PedirHoraController.class).obtener(id, token))
                        .withSelfRel()
        );

        recurso.add(
                linkTo(methodOn(PedirHoraController.class).listar(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(PedirHoraController.class).actualizar(id, null, token))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(PedirHoraController.class).eliminar(id))
                        .withRel("delete")
        );

        return ResponseEntity.ok(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .data(pedirHoraService.obtener(id, token))
                        .build()
        );
    }

    @Operation(
        summary = "Actualizar hora medica por ID",
        description = "Modifica una hora medica que haya tomado un paciente. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Hora medica actualizados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedirHoraDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .message("Se cambio la hora")
                        .data(pedirHoraService.actualizar(id, dto, token))
                        .build()
        );
    }

   @Operation(
        summary = "Eliminar (anular) una hora medica por ID",
        description = "Elimina una hora medica que haya pedido un paciente. Requiere rol USER o ADMIN."
    )
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Hora medica eliminada/anulada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        pedirHoraService.eliminar(id);

        return ResponseEntity.status(200).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se anulo la hora")
                        .build()
        );
    }
}