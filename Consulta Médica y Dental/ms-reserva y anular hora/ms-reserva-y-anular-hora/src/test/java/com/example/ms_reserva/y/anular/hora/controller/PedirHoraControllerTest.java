package com.example.ms_reserva.y.anular.hora.controller;

import com.example.ms_reserva.y.anular.hora.dto.*;
import com.example.ms_reserva.y.anular.hora.security.JwtUtil;
import com.example.ms_reserva.y.anular.hora.service.PedirHoraService;
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
class PedirHoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedirHoraService pedirHoraService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarReservas() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        PedirHoraResponse response = PedirHoraResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .fecha(LocalDate.of(2026, 6, 20))
                .horaDeAtencion(LocalTime.of(10, 30))
                .atencion("Consulta dental")
                .build();

        when(pedirHoraService.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].atencion").value("Consulta dental"))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeObtenerReservaPorId() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        PedirHoraResponse response = PedirHoraResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .fecha(LocalDate.of(2026, 6, 20))
                .horaDeAtencion(LocalTime.of(10, 30))
                .atencion("Consulta dental")
                .build();

        when(pedirHoraService.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeCrearReserva() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHoraDeAtencion(LocalTime.of(10, 30));
        dto.setAtencion("Consulta dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        PedirHoraResponse response = PedirHoraResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .fecha(LocalDate.of(2026, 6, 20))
                .horaDeAtencion(LocalTime.of(10, 30))
                .atencion("Consulta dental")
                .build();

        when(pedirHoraService.crear(any(PedirHoraDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se reservo la hora"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarReserva() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PedirHoraDTO dto = new PedirHoraDTO();
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

        PedirHoraResponse response = PedirHoraResponse.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .fecha(LocalDate.of(2026, 6, 25))
                .horaDeAtencion(LocalTime.of(11, 0))
                .atencion("Control dental")
                .build();

        when(pedirHoraService.actualizar(eq(1L), any(PedirHoraDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/reservar-y-anular-hora/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se cambio la hora"))
                .andExpect(jsonPath("$.data.atencion").value("Control dental"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeEliminarReserva() throws Exception {
        doNothing().when(pedirHoraService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/reservar-y-anular-hora/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se anulo la hora"));
    }
}