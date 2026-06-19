package com.example.ms_opinion.del.paciente.controller;

import com.example.ms_opinion.del.paciente.dto.MedicoResponse;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
import com.example.ms_opinion.del.paciente.dto.PacienteResponse;
import com.example.ms_opinion.del.paciente.security.JwtUtil;
import com.example.ms_opinion.del.paciente.service.OpinionPacienteService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OpinionPacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OpinionPacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpinionPacienteService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    private static final String BASE_URL = "/api/v1/opiniones";

    // Helper para construir el response reutilizable
    private OpinionPacienteResponse buildResponse() {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        return OpinionPacienteResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .atencionMedico(8)
                .expliqueSuPuntuacion("muy amable")
                .explicacionTratamiento("bastante claro")
                .comentarioMejora("comentario")
                .puntuacionMedico(8)
                .build();
    }

    // Helper para construir el DTO reutilizable
    private OpinionPacienteDTO buildDTO() {
        OpinionPacienteDTO dto = new OpinionPacienteDTO();
        dto.setRunPaciente("11111111-1");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setAtencionMedico(8);
        dto.setExpliqueSuPuntuacion("muy amable");
        dto.setExplicacionTratamiento("bastante claro");
        dto.setComentarioMejora("comentario");
        dto.setPuntuacionMedico(8);
        return dto;
    }

    @Test
    void debeListarOpinionPaciente() throws Exception {
        when(service.listar(anyString())).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado de opiniones obtenido"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].atencionMedico").value(8))
                .andExpect(jsonPath("$.data[0].puntuacionMedico").value(8));
    }

    @Test
    void debeObtenerOpinionPacientePorId() throws Exception {
        when(service.obtener(eq(1L), anyString())).thenReturn(buildResponse());

        mockMvc.perform(get(BASE_URL + "/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Detalle de la opinión obtenido"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.atencionMedico").value(8));

        verify(service, times(1)).obtener(eq(1L), anyString());
    }

    @Test
    void debeCrearOpinionPaciente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(service.crear(any(OpinionPacienteDTO.class), anyString())).thenReturn(buildResponse());

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Opinión registrada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarOpinionPaciente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(service.actualizar(eq(1L), any(OpinionPacienteDTO.class), anyString()))
                .thenReturn(buildResponse());

        mockMvc.perform(put(BASE_URL + "/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Opinión actualizada exitosamente"))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeEliminarOpinionPaciente() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("La opinión ha sido eliminada"));
    }
}