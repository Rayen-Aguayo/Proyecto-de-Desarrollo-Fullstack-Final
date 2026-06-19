package test.java.com.example.ms_ficha.medica.controller;

import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.dto.MedicoResponse;
import com.example.ms_ficha.medica.dto.PacienteResponse;
import com.example.ms_ficha.medica.security.JwtUtil;
import com.example.ms_ficha.medica.service.FichaMedicaService;
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

@WebMvcTest(FichaMedicaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FichaMedicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FichaMedicaService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    // Helpers reutilizables
    private FichaMedicaResponse buildResponse() {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");
        paciente.setAlergias("alergias");
        paciente.setEnfermedad("enfermedad");
        paciente.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        return FichaMedicaResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .procedimiento("procedimiento")
                .odontograma("odontograma")
                .build();
    }

    private FichaMedicaDTO buildDTO() {
        FichaMedicaDTO dto = new FichaMedicaDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setProcedimiento("procedimiento");
        dto.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");
        dto.setEnfermedad("enfermedad");
        dto.setAlergias("alergias");
        dto.setOdontograma("odontograma");
        return dto;
    }

    @Test
    void debeListarFichaMedica() throws Exception {
        when(service.listar(anyString())).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/api/v1/fichas_medicas")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data[0].procedimiento").value("procedimiento"));
    }

    @Test
    void debeObtenerFichaMedicaPorId() throws Exception {
        when(service.obtener(eq(1L), anyString())).thenReturn(buildResponse());

        mockMvc.perform(get("/api/v1/fichas_medicas/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ficha médica encontrada"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.procedimiento").value("procedimiento"));

        verify(service, times(1)).obtener(eq(1L), anyString());
    }

    @Test
    void debeCrearFichaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(service.crear(any(FichaMedicaDTO.class), anyString())).thenReturn(buildResponse());

        mockMvc.perform(post("/api/v1/fichas_medicas")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ficha médica creada"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarFichaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(service.actualizar(eq(1L), any(FichaMedicaDTO.class), anyString()))
                .thenReturn(buildResponse());

        mockMvc.perform(put("/api/v1/fichas_medicas/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se actualizó la ficha médica"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.procedimiento").value("procedimiento"));
    }

    @Test
    void debeEliminarFichaMedica() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/fichas_medicas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se eliminó la ficha médica"));
    }
}