package test.java.com.example.ms_medico.controller;


import com.example.ms_medico.model.Medico;
import com.example.ms_medico.service.MedicoService;
import com.example.ms_paciente.model.Paciente;
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

public class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicoService Service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarMedico() throws Exception {
        List<Medico> medico = List.of(            
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );


        when(service.listar()).thenReturn(medico);

        mockMvc.perform(get("api/v1/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].run").value("22222222-2"))
                .andExpect(jsonPath("$.data[0].nombre").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("987654321"));

    }

    @Test
    void debeObtenerMedicoPorId() throws Exception {
        Medico medico = new Medico(   
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );


        when(service.obtener("22222222-2")).thenReturn(autor);

        mockMvc.perform(get("api/v1/medicos/22222222-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico obtenido"))
                .andExpect(jsonPath("$.data[0].run").value("22222222-2"))
                .andExpect(jsonPath("$.data[0].nombre").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("987654321"));
    }

    @Test
    void debeCrearMedico() throws Exception {
        MedicoDTO dto = new MedicoDTO();

        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setEdad(28); 
        dto.setNroTelefono("987654321");
        dto.setEspecialidad("cirujano");
        dto.setFirmaMedico("firma-soto");

        Medico creado = new Medico("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );


        when(service.crear(any(PacienteDTO.class))).thenReturn(creado);

        mockMvc.perform(post("api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico creado"))
                .andExpect(jsonPath("$.data[0].run").value("22222222-2"))
                .andExpect(jsonPath("$.data[0].nombre").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("987654321"));
    }

    @Test
    void debeActualizarMedico() throws Exception {
        MedicoDTO dto = new MedicoDTO();
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setEdad(28); 
        dto.setNroTelefono("987654321");
        dto.setEspecialidad("cirujano");
        dto.setFirmaMedico("firma-soto");

        Medico actualizado = new Medico(
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );


        when(service.actualizar(eq("22222222-2"), any(PacienteDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("api/v1/medicos/22222222-2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medico actualizado"))
                .andExpect(jsonPath("$.data[0].run").value("22222222-2"))
                .andExpect(jsonPath("$.data[0].nombre").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].datos").value(28))
                .andExpect(jsonPath("$.data[0].nroTelefono").value("987654321"));
    }

    @Test
    void debeEliminarMedico() throws Exception {
        doNothing().when(service).eliminar("22222222-2");

        mockMvc.perform(delete("api/v1/medicos/22222222-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("MEdico eliminado"));
    }
}

