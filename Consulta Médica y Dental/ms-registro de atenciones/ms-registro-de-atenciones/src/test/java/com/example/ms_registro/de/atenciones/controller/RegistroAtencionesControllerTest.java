package com.example.ms_registro.de.atenciones.controller;

import com.example.ms_registro.de.atenciones.dto.*;
import com.example.ms_registro.de.atenciones.security.JwtUtil;
import com.example.ms_registro.de.atenciones.service.RegistroAtencionesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(RegistroAtencionesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RegistroAtencionesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistroAtencionesService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarRegistrosDeAtenciones() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pago = new PagosResponse();
        pago.setId(1L);
        pago.setTotal(50000.0);
        pago.setEstado("PAGADO");

        RegistroAtencionesResponse response = RegistroAtencionesResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .pago(pago)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .tratamientoRealizado("Limpieza dental")
                .build();

        when(service.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/registro-atenciones")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].tratamientoRealizado").value("Limpieza dental"))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeObtenerRegistroPorId() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pago = new PagosResponse();
        pago.setId(1L);
        pago.setTotal(50000.0);
        pago.setEstado("PAGADO");

        RegistroAtencionesResponse response = RegistroAtencionesResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .pago(pago)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .tratamientoRealizado("Limpieza dental")
                .build();

        when(service.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/registro-atenciones/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.tratamientoRealizado").value("Limpieza dental"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeCrearRegistroDeAtenciones() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setNompaciente("Juan Pérez");
        dto.setRunpaciente("11111111-1");
        dto.setNommedico("Dra. Soto");
        dto.setRunmedico("22222222-2");
        dto.setTotal(50000.0);
        dto.setIdPago(1);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHora(LocalTime.of(10, 30));
        dto.setTratamientoRealizado("Limpieza dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pago = new PagosResponse();
        pago.setId(1L);
        pago.setTotal(50000.0);
        pago.setEstado("PAGADO");

        RegistroAtencionesResponse response = RegistroAtencionesResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .pago(pago)
                .fecha(LocalDate.of(2026, 6, 20))
                .hora(LocalTime.of(10, 30))
                .tratamientoRealizado("Limpieza dental")
                .build();

        when(service.crear(any(RegistroAtencionesDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/registro-atenciones")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro de atenciones creado"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.tratamientoRealizado").value("Limpieza dental"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeActualizarRegistroDeAtenciones() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setNompaciente("Juan Pérez");
        dto.setRunpaciente("11111111-1");
        dto.setNommedico("Dra. Soto");
        dto.setRunmedico("22222222-2");
        dto.setTotal(60000.0);
        dto.setIdPago(1);
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHora(LocalTime.of(11, 0));
        dto.setTratamientoRealizado("Control dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pago = new PagosResponse();
        pago.setId(1L);
        pago.setTotal(60000.0);
        pago.setEstado("PAGADO");

        RegistroAtencionesResponse response = RegistroAtencionesResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .pago(pago)
                .fecha(LocalDate.of(2026, 6, 25))
                .hora(LocalTime.of(11, 0))
                .tratamientoRealizado("Control dental")
                .build();

        when(service.actualizar(eq(1L), any(RegistroAtencionesDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/v1/registro-atenciones/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro de atenciones actualizado"))
                .andExpect(jsonPath("$.data.tratamientoRealizado").value("Control dental"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeEliminarRegistroDeAtenciones() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/registro-atenciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro de atenciones eliminado"));
    }
}