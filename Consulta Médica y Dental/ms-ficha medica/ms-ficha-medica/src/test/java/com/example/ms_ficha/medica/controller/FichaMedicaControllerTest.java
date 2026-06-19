package test.java.com.example.ms_ficha.medica.controller;


import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.dto.PacienteResponse;
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
public class FichaMedicaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FichaMedicaService Service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarFichaMedica() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");
        paciente.setAlergias("alergias");
        paciente.setEnfermedad("enfermedad");
        paciente.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        FichaMedicaResponse response = FichaMedicaResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .procedimiento("procedimiento")
                .odontograma("odontograma")
                .build();

        when(Service.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/fichas_medicas")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeObtenerFichaMedicaPorId() throws Exception {
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

        FichaMedicaResponse response = FichaMedicaResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .procedimiento("procedimiento")
                .odontograma("odontograma")
                .build();

        when(Service.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/fichas_medicas/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeCrearFichaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        FichaMedicaDTO dto = new FichaMedicaDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setProcedimiento("Procedimiento");
        dto.setQueMedicamentoEstaTomando("QueMedicamentoEstaTomando");
        dto.setEnfermedad("Enfermedad");
        dto.setAlergias("Alergias");
        dto.setOdontograma("Odontograma");

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

        FichaMedicaResponse response = FichaMedicaResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .procedimiento("procedimiento")
                .odontograma("odontograma")
                .build();

        when(Service.crear(any(FichaMedicaDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se creo la ficha medica"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarFichaMedica() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        FichaMedicaDTO dto = new FichaMedicaDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHoraDeAtencion(LocalTime.of(11, 0));
        dto.setAtencion("Control dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        FichaMedicaResponse response = FichaMedicaResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .procedimiento("procedimiento")
                .odontograma("odontograma")
                .build();

        when(Service.actualizar(eq(1L), any(FichaMedicaDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/fichas_medicas/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se actualizo la ficha medica"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeEliminarFichaMedica() throws Exception {
        doNothing().when(Service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/fichas_medicas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se elimino la ficha medica"));
    }
}
