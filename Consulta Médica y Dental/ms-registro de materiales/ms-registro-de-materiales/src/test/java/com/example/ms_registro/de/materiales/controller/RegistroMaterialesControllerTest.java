package com.example.ms_registro.de.materiales.controller;

import com.example.ms_registro.de.materiales.dto.RegistroMaterialesDTO;
import com.example.ms_registro.de.materiales.model.RegistroMateriales;
import com.example.ms_registro.de.materiales.security.JwtUtil;
import com.example.ms_registro.de.materiales.service.RegistroMaterialesService;
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

@WebMvcTest(RegistroMaterialesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RegistroMaterialesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistroMaterialesService registroMaterialesService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarRegistrosMateriales() throws Exception {
        RegistroMateriales registro = new RegistroMateriales(
            1L, 10, "Guantes, Mascarillas", LocalDate.of(2027, 1, 1)
        );

        when(registroMaterialesService.listar()).thenReturn(List.of(registro));

        mockMvc.perform(get("/api/v1/registrosMateriales")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nombresProductos").value("Guantes, Mascarillas"))
                .andExpect(jsonPath("$.data[0].cantidadProductos").value(10));
    }

    @Test
    void debeObtenerRegistroMaterialesPorId() throws Exception {
        RegistroMateriales registro = new RegistroMateriales(
            1L, 10, "Guantes, Mascarillas", LocalDate.of(2027, 1, 1)
        );

        when(registroMaterialesService.obtener(eq(1L))).thenReturn(registro);

        mockMvc.perform(get("/api/v1/registrosMateriales/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombresProductos").value("Guantes, Mascarillas"));
    }

    @Test
    void debeCrearRegistroMateriales() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RegistroMaterialesDTO dto = new RegistroMaterialesDTO();
        dto.setCantidadProductos(10);
        dto.setNombresProductos("Guantes, Mascarillas");
        dto.setFechaCaducidadProductos(LocalDate.of(2027, 1, 1));

        RegistroMateriales registro = new RegistroMateriales(
            1L, 10, "Guantes, Mascarillas", LocalDate.of(2027, 1, 1)
        );

        when(registroMaterialesService.crear(any(RegistroMaterialesDTO.class))).thenReturn(registro);

        mockMvc.perform(post("/api/v1/registrosMateriales")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("registro creado"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombresProductos").value("Guantes, Mascarillas"));
    }

    @Test
    void debeActualizarRegistroMateriales() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RegistroMaterialesDTO dto = new RegistroMaterialesDTO();
        dto.setCantidadProductos(20);
        dto.setNombresProductos("Jeringas");
        dto.setFechaCaducidadProductos(LocalDate.of(2028, 6, 1));

        RegistroMateriales registro = new RegistroMateriales(
            1L, 20, "Jeringas", LocalDate.of(2028, 6, 1)
        );

        when(registroMaterialesService.actualizar(eq(1L), any(RegistroMaterialesDTO.class))).thenReturn(registro);

        mockMvc.perform(put("/api/v1/registrosMateriales/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("registro actualizado"))
                .andExpect(jsonPath("$.data.nombresProductos").value("Jeringas"))
                .andExpect(jsonPath("$.data.cantidadProductos").value(20));
    }

    @Test
    void debeEliminarRegistroMateriales() throws Exception {
        doNothing().when(registroMaterialesService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/registrosMateriales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("registro eliminado"));
    }
}
