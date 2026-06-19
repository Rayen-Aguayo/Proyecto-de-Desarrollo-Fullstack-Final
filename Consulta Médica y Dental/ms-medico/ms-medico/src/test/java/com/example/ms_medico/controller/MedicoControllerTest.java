package com.example.ms_medico.controller;

import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.security.JwtUtil;
import com.example.ms_medico.service.MedicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(MedicoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicoService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    // Helper reutilizable
    private Medico buildMedico() {
        return new Medico(
                "22222222-2",
                "Dra. Soto",
                28,
                "987654321",
                "cirujano",
                "firma-soto"
        );
    }

    private MedicoDTO buildDTO() {
        MedicoDTO dto = new MedicoDTO();
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setEdad(28);
        dto.setNroTelefono("987654321");
        dto.setEspecialidad("cirujano");
        dto.setFirmaMedico("firma-soto");
        return dto;
    }

    @Test
    void debeListarMedico() throws Exception {
        when(service.listar()).thenReturn(List.of(buildMedico()));

        mockMvc.perform(get("/api/v1/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].runMedico").value("22222222-2"))
                .andExpect(jsonPath("$.data[0].nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].edad").value(28))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("987654321"))
                .andExpect(jsonPath("$.data[0].especialidad").value("cirujano"));
    }

    @Test
    void debeObtenerMedicoPorId() throws Exception {
        when(service.obtener("22222222-2")).thenReturn(buildMedico());

        mockMvc.perform(get("/api/v1/medicos/22222222-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico obtenido"))
                .andExpect(jsonPath("$.data.runMedico").value("22222222-2"))
                .andExpect(jsonPath("$.data.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.edad").value(28))
                .andExpect(jsonPath("$.data.nroTelefono").value("987654321"))
                .andExpect(jsonPath("$.data.especialidad").value("cirujano"));

        verify(service, times(1)).obtener("22222222-2");
    }

    @Test
    void debeCrearMedico() throws Exception {
        when(service.crear(any(MedicoDTO.class))).thenReturn(buildMedico());

        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico creado"))
                .andExpect(jsonPath("$.data.runMedico").value("22222222-2"))
                .andExpect(jsonPath("$.data.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.edad").value(28))
                .andExpect(jsonPath("$.data.nroTelefono").value("987654321"));
    }

    @Test
    void debeActualizarMedico() throws Exception {
        when(service.actualizar(eq("22222222-2"), any(MedicoDTO.class))).thenReturn(buildMedico());

        mockMvc.perform(put("/api/v1/medicos/22222222-2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico actualizado"))
                .andExpect(jsonPath("$.data.runMedico").value("22222222-2"))
                .andExpect(jsonPath("$.data.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.edad").value(28))
                .andExpect(jsonPath("$.data.nroTelefono").value("987654321"));
    }

    @Test
    void debeEliminarMedico() throws Exception {
        doNothing().when(service).eliminar("22222222-2");

        mockMvc.perform(delete("/api/v1/medicos/22222222-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico eliminado"));
    }
}