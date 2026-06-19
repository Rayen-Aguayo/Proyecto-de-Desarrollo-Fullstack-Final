package com.example.ms_pagos.controller;

import com.example.ms_pagos.dto.*;
import com.example.ms_pagos.exception.GlobalExceptionHandler;
import com.example.ms_pagos.security.JwtUtil;
import com.example.ms_pagos.service.PagosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagosController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PagosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagosService pagosService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarPagos() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        PagosResponse response = PagosResponse.builder()
                .id(1L)
                .paciente(paciente)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .metodoPago("Transferencia")
                .nroBoleta(1001)
                .registroFacturacion("FAC-001")
                .neto(42017.0)
                .iva(7983.0)
                .total(50000.0)
                .estado("PAGADO")
                .build();

        when(pagosService.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/pagos")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].estado").value("PAGADO"))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeObtenerPagoPorId() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        PagosResponse response = PagosResponse.builder()
                .id(1L)
                .paciente(paciente)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .metodoPago("Transferencia")
                .nroBoleta(1001)
                .registroFacturacion("FAC-001")
                .neto(42017.0)
                .iva(7983.0)
                .total(50000.0)
                .estado("PAGADO")
                .build();

        when(pagosService.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/pagos/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.estado").value("PAGADO"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeCrearPago() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PagosDTO dto = new PagosDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHora(LocalTime.of(10, 30));
        dto.setMetodoPago("Transferencia");
        dto.setNroBoleta(1001);
        dto.setRegistroFacturacion("FAC-001");
        dto.setNeto(42017.0);
        dto.setIva(7983.0);
        dto.setTotal(50000.0);
        dto.setEstado("PAGADO");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        PagosResponse response = PagosResponse.builder()
                .id(1L)
                .paciente(paciente)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .metodoPago("Transferencia")
                .nroBoleta(1001)
                .registroFacturacion("FAC-001")
                .neto(42017.0)
                .iva(7983.0)
                .total(50000.0)
                .estado("PAGADO")
                .build();

        when(pagosService.crear(any(PagosDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/pagos")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pago registrado"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.estado").value("PAGADO"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeActualizarPago() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PagosDTO dto = new PagosDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHora(LocalTime.of(11, 0));
        dto.setMetodoPago("Efectivo");
        dto.setNroBoleta(1002);
        dto.setRegistroFacturacion("FAC-002");
        dto.setNeto(42017.0);
        dto.setIva(7983.0);
        dto.setTotal(50000.0);
        dto.setEstado("PENDIENTE");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        PagosResponse response = PagosResponse.builder()
                .id(1L)
                .paciente(paciente)
                .fecha(LocalDate.of(2026, 6, 25))
                .hora(LocalTime.of(11, 0))
                .metodoPago("Efectivo")
                .nroBoleta(1002)
                .registroFacturacion("FAC-002")
                .neto(42017.0)
                .iva(7983.0)
                .total(50000.0)
                .estado("PENDIENTE")
                .build();

        when(pagosService.actualizar(eq(1L), any(PagosDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/v1/pagos/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pago actualizado"))
                .andExpect(jsonPath("$.data.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeRetornar404AlObtenerPagoInexistente() throws Exception {
        when(pagosService.obtener(eq(999L), anyString()))
                .thenThrow(new RuntimeException("Pago no encontrado"));

        mockMvc.perform(get("/api/v1/pagos/999")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Pago no encontrado"));
    }

    @Test
    void debeRetornar404AlActualizarPagoInexistente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // DTO completo y válido para que pase la validación y llegue al service
        PagosDTO dto = new PagosDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHora(LocalTime.of(11, 0));
        dto.setMetodoPago("Efectivo");
        dto.setNroBoleta(1002);
        dto.setRegistroFacturacion("FAC-002");
        dto.setNeto(42017.0);
        dto.setIva(7983.0);
        dto.setTotal(50000.0);
        dto.setEstado("PENDIENTE");

        when(pagosService.actualizar(eq(999L), any(PagosDTO.class), anyString()))
                .thenThrow(new RuntimeException("Pago no encontrado"));

        mockMvc.perform(put("/api/v1/pagos/999")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Pago no encontrado"));
    }

    @Test
    void debeRetornar400AlCrearPagoConCamposNulos() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PagosDTO dto = new PagosDTO();

        mockMvc.perform(post("/api/v1/pagos")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validación fallida"));
    }

    @Test
    void debeRetornar400AlActualizarPagoConCamposNulos() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PagosDTO dto = new PagosDTO();

        mockMvc.perform(put("/api/v1/pagos/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validación fallida"));
    }

    @Test
    void debeRetornarListaVaciaAlNoHaberPagos() throws Exception {
        when(pagosService.listar(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pagos")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}