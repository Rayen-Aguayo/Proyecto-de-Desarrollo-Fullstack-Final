package com.example.ms_facturacion.y.presupuesto.Controller;


import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoDTO;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoResponse;
import com.example.ms_facturacion.y.presupuesto.dto.MedicoResponse;
import com.example.ms_facturacion.y.presupuesto.dto.PacienteResponse;
import com.example.ms_facturacion.y.presupuesto.security.JwtUtil;
import com.example.ms_facturacion.y.presupuesto.service.FacturacionYPresupuestoService;
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

@WebMvcTest(FacturacionYPresupuestoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FacturacionYPresupuestoController {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacturacionYPresupuestoService service;

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

        FacturacionYPresupuestoResponse response = FacturacionYPresupuestoResponse.builder()
                .id(1L)
                .presupuesto(30.000)
                .paciente(paciente)
                .medico(medico)
                .tratamiento("tratamiento")
                .diasDuracion(8)
                .gestionPagos("gestion pagos")
                .build();

        when(service.listar(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/facturacio-y-presupuesto")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].atencion").value("facturacio y presupuesto"))
                .andExpect(jsonPath("$.data[0].paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeObtenerFacturacionYPresupuestoPorId() throws Exception {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
       

        FacturacionYPresupuestoResponse response = FacturacionYPresupuestoResponse.builder()
                .id(1L)
                .presupuesto(30.000)
                .paciente(paciente)
                .medico(medico)
                .tratamiento("tratamiento")
                .diasDuracion(8)
                .gestionPagos("gestion pagos")
                .build();

        when(service.obtener(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/facturacio-y-presupuesto/1")
                        .header("Authorization", "Bearer token-de-prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeCrearReserva() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
        dto.setPresupuesto(30.000);
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setTratamiento("tratamiento");
        dto.setDiasDuracion(8);
        dto.setGestionPagos("gestion pagos");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        FacturacionYPresupuestoResponse response = FacturacionYPresupuestoResponse.builder()
                .id(1L)
                .presupuesto(30.000)
                .paciente(paciente)
                .medico(medico)
                .tratamiento("tratamiento")
                .diasDuracion(8)
                .gestionPagos("gestion pagos")
                .build();

        when(service.crear(any(FacturacionYPresupuestoDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/facturacio-y-presupuesto")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se creo un facturacio y presupuesto"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"));
    }

    @Test
    void debeActualizarFacturacionYPresupuesto() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
        dto.setPresupuesto(30.000);
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setTratamiento("tratamiento");
        dto.setDiasDuracion(8);
        dto.setGestionPagos("gestion pagos");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        FacturacionYPresupuestoResponse response = FacturacionYPresupuestoResponse.builder()
                .id(1L)
                .presupuesto(30.000)
                .paciente(paciente)
                .medico(medico)
                .tratamiento("tratamiento")
                .diasDuracion(8)
                .gestionPagos("gestion pagos")
                .build();

        when(service.actualizar(eq(1L), any(FacturacionYPresupuestoDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/facturacio-y-presupuesto/1")
                        .header("Authorization", "Bearer token-de-prueba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se actualizo facturacio y presupuesto"))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeEliminarFacturacionYPresupuesto() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/facturacio-y-presupuesto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("se elimino facturacio y presupuesto"));
    }
}
