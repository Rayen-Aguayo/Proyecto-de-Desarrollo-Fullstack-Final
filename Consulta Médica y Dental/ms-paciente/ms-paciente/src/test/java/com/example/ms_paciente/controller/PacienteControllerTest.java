package com.example.ms_paciente.controller;


import com.example.ms_paciente.dto.PacienteDTO;
import com.example.ms_paciente.model.Paciente;
import com.example.ms_paciente.service.PacienteService;
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

@WebMvcTest(PedirHoraController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PacienteService service;

    
    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarPacientes() throws Exception {
        List<Paciente> pacientes = List.of(
                new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        ));


        when(service.listar()).thenReturn(pacientes);

        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].run").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].nombre").value("paciente"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].edad").value("datos del paciente"))
                .andExpect(jsonPath("$.data[0].alergias").value("alergias"))
                .andExpect(jsonPath("$.data[0].enfermedad").value("enfermedad"))
                .andExpect(jsonPath("$.data[0].queMedicamentoEstaTomando").value("medicamento"))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("123456789"));

    }

    @Test
    void debeObtenerPacientePorId() throws Exception {
        Paciente paciente = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );


        when(service.obtener("11111111-1")).thenReturn(paciente);

        mockMvc.perform(get("/api/v1/pacientes/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Paciente obtenido"))
                .andExpect(jsonPath("$.data[0].run").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].nombre").value("paciente"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].edad").value("datos del paciente"))
                .andExpect(jsonPath("$.data[0].alergias").value("alergias"))
                .andExpect(jsonPath("$.data[0].enfermedad").value("enfermedad"))
                .andExpect(jsonPath("$.data[0].queMedicamentoEstaTomando").value("medicamento"))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("123456789"));
    }

    @Test
    void debeCrearPaciente() throws Exception {
        PacienteDTO dto = new PacienteDTO();

        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("paciente");
        dto.setDatosDelPaciente("datos del paciente");
        dto.setEdadPaciente(28);
        dto.setAlergias("alergias"); 
        dto.setEnfermedad("enfermedad"); 
        dto.setQueMedicamentoEstaTomando("medicamento"); 
        dto.setNroTelefono("123456789");

        Paciente creado = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );


        when(service.crear(any(PacienteDTO.class))).thenReturn(creado);

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Paciente creado"))
                .andExpect(jsonPath("$.data[0].run").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].nombre").value("paciente"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].edad").value("datos del paciente"))
                .andExpect(jsonPath("$.data[0].alergias").value("alergias"))
                .andExpect(jsonPath("$.data[0].enfermedad").value("enfermedad"))
                .andExpect(jsonPath("$.data[0].queMedicamentoEstaTomando").value("medicamento"))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("123456789"));
    }

    @Test
    void debeActualizarPaciente() throws Exception {
        PacienteDTO dto = new PacienteDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("paciente");
        dto.setDatosDelPaciente("datos del paciente");
        dto.setEdadPaciente(28);
        dto.setAlergias("alergias"); 
        dto.setEnfermedad("enfermedad"); 
        dto.setQueMedicamentoEstaTomando("medicamento"); 
        dto.setNroTelefono("123456789");

        Paciente actualizado = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );


        when(service.actualizar(eq("11111111-1"), any(PacienteDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/pacientes/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Paciente actualizado"))
                .andExpect(jsonPath("$.data[0].run").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].nombre").value("paciente"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].edad").value("datos del paciente"))
                .andExpect(jsonPath("$.data[0].alergias").value("alergias"))
                .andExpect(jsonPath("$.data[0].enfermedad").value("enfermedad"))
                .andExpect(jsonPath("$.data[0].queMedicamentoEstaTomando").value("medicamento"))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("123456789"));
    }

    @Test
    void debeEliminarPaciente() throws Exception {
        doNothing().when(service).eliminar("11111111-1");

        mockMvc.perform(delete("/api/v1/pacientes/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Paciente eliminado"));
    }
}

