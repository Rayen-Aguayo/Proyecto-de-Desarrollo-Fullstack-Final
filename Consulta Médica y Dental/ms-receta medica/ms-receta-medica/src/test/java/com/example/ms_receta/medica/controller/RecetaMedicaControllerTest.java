package com.example.ms_receta.medica.controller;

import com.example.ms_receta.medica.dto.*;
import com.example.ms_receta.medica.security.JwtUtil;
import com.example.ms_receta.medica.service.RecetaMedicaService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecetaMedicaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RecetaMedicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecetaMedicaService recetamedicaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarRecetasMedicas() throws Exception {
        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dr. Rojas");
        medico.setFirmaMedico("firma-rojas");

        RecetaMedicaResponce response = RecetaMedicaResponce.builder()
                .id(1L)
                .nomMedicamento("Amoxicilina")
                .diasTomarMedicamento(7)
                .inicioReceta(LocalDate.of(2026, 6, 20))
                .medico(medico)
                .cantTomarDia(3)
                .build();

        when(recetamedicaService.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/recetasMedicas")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nomMedicamento").value("Amoxicilina"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dr. Rojas"));
    }

    @Test
    void debeObtenerRecetaPorId() throws Exception {
        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dr. Rojas");
        medico.setFirmaMedico("firma-rojas");

        RecetaMedicaResponce response = RecetaMedicaResponce.builder()
                .id(1L)
                .nomMedicamento("Amoxicilina")
                .diasTomarMedicamento(7)
                .inicioReceta(LocalDate.of(2026, 6, 20))
                .medico(medico)
                .cantTomarDia(3)
                .build();

        when(recetamedicaService.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/recetasMedicas/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nomMedicamento").value("Amoxicilina"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dr. Rojas"));
    }

    @Test
    void debeCrearRecetaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setNomMedicamento("Amoxicilina");
        dto.setDiasTomarMedicamento(7);
        dto.setInicioReceta(LocalDate.of(2026, 6, 20));
        dto.setNomMedico("Dr. Rojas");
        dto.setRunMedico("22222222-2");
        dto.setCantTomarDia(3);
        dto.setFirmaMedico("firma-rojas");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dr. Rojas");
        medico.setFirmaMedico("firma-rojas");

        RecetaMedicaResponce response = RecetaMedicaResponce.builder()
                .id(1L)
                .nomMedicamento("Amoxicilina")
                .diasTomarMedicamento(7)
                .inicioReceta(LocalDate.of(2026, 6, 20))
                .medico(medico)
                .cantTomarDia(3)
                .build();

        when(recetamedicaService.crear(any(RecetaMedicaDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/recetasMedicas")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Receta médica creada"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nomMedicamento").value("Amoxicilina"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dr. Rojas"));
    }

    @Test
    void debeActualizarRecetaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setNomMedicamento("Ibuprofeno");
        dto.setDiasTomarMedicamento(5);
        dto.setInicioReceta(LocalDate.of(2026, 6, 25));
        dto.setNomMedico("Dr. Rojas");
        dto.setRunMedico("22222222-2");
        dto.setCantTomarDia(2);
        dto.setFirmaMedico("firma-rojas");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dr. Rojas");
        medico.setFirmaMedico("firma-rojas");

        RecetaMedicaResponce response = RecetaMedicaResponce.builder()
                .id(1L)
                .nomMedicamento("Ibuprofeno")
                .diasTomarMedicamento(5)
                .inicioReceta(LocalDate.of(2026, 6, 25))
                .medico(medico)
                .cantTomarDia(2)
                .build();

        when(recetamedicaService.actualizar(eq(1L), any(RecetaMedicaDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/v1/recetasMedicas/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Receta médica actualizada"))
                .andExpect(jsonPath("$.data.nomMedicamento").value("Ibuprofeno"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dr. Rojas"));
    }

    @Test
    void debeEliminarRecetaMedica() throws Exception {
        doNothing().when(recetamedicaService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/recetasMedicas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Receta médica eliminada"));
    }
}