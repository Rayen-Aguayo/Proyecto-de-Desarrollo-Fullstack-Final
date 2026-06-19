package test.java.com.example.ms_opinion.del.paciente.controller;


import com.example.ms_ficha.medica.dto.PacienteResponse;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecetaMedicaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OpinionPacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpinionPacienteService Service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarOpinionPaciente() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        OpinionPacienteResponse response = OpinionPacienteResponse.builder()
                .id(1L)
            .runPaciente("11111111-1")
            .runMedico("22222222-2")
            .nombreMedico("Dra. Soto")
            .atencionMedico(8)
            .expliqueSuPuntuacion("muy amable")
            .explicacionTratamiento("bastante claro")
            .comentarioMejora("comentario")
            .puntuacionMedico(8)
            .build();

        when(service.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/facturacio-y-presupuesto")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeObtenerOpinionPacientePorId() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
       

        OpinionPacienteResponse response = OpinionPacienteResponse.builder()
                .id(1L)
                .runPaciente("11111111-1")
                .runMedico("22222222-2")
                .nombreMedico("Dra. Soto")
                .atencionMedico(8)
                .expliqueSuPuntuacion("muy amable")
                .explicacionTratamiento("bastante claro")
                .comentarioMejora("comentario")
                .puntuacionMedico(8)
                .build();

        when(service.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/facturacio-y-presupuesto/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeCrearOpinionPaciente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        OpinionPacienteDTO dto = new OpinionPacienteDTO();
            dto.setRunPaciente("11111111-1");
            dto.setRunMedico("22222222-2");
            dto.setNombreMedico("Dra. Soto");
            dto.setAtencionMedico(8);
            dto.setExpliqueSuPuntuacion("muy amable");
            dto.setExplicacionTratamiento("bastante claro");
            dto.setComentarioMejora("comentario");
            dto.setPuntuacionMedico(8);

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        OpinionPacienteResponse response = OpinionPacienteResponse.builder()
                .id(1L)
                .runPaciente("11111111-1")
                .runMedico("22222222-2")
                .nombreMedico("Dra. Soto")
                .atencionMedico(8)
                .expliqueSuPuntuacion("muy amable")
                .explicacionTratamiento("bastante claro")
                .comentarioMejora("comentario")
                .puntuacionMedico(8)
                .build();

        when(service.crear(any(OpinionPacienteDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/facturacio-y-presupuesto")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se creo un facturacio y presupuesto"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarOpinionPaciente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        OpinionPacienteDTO dto = new OpinionPacienteDTO();
            dto.setRunPaciente("11111111-1");
            dto.setRunMedico("22222222-2");
            dto.setNombreMedico("Dra. Soto");
            dto.setAtencionMedico(8);
            dto.setExpliqueSuPuntuacion("muy amable");
            dto.setExplicacionTratamiento("bastante claro");
            dto.setComentarioMejora("comentario");
            dto.setPuntuacionMedico(8);

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        OpinionPacienteResponse response = OpinionPacienteResponse.builder()
                .id(1L)
                .runPaciente("11111111-1")
                .runMedico("22222222-2")
                .nombreMedico("Dra. Soto")
                .atencionMedico(8)
                .expliqueSuPuntuacion("muy amable")
                .explicacionTratamiento("bastante claro")
                .comentarioMejora("comentario")
                .puntuacionMedico(8)
                .build();

        when(service.actualizar(eq(1L), any(OpinionPacienteDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/facturacio-y-presupuesto/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se actualizo facturacio y presupuesto"))
                .andExpect(jsonPath("$.data.paciente.runPaciente").value("11111111-1"));
    }

    @Test
    void debeEliminarOpinionPaciente() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/facturacio-y-presupuesto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se elimino opinion del paciente"));
    }
}
