package com.example.ms_registro.de.atenciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_registro.de.atenciones.client.MedicoClient;
import com.example.ms_registro.de.atenciones.client.PacienteClient;
import com.example.ms_registro.de.atenciones.client.PagosClient;
import com.example.ms_registro.de.atenciones.dto.MedicoResponse;
import com.example.ms_registro.de.atenciones.dto.PacienteResponse;
import com.example.ms_registro.de.atenciones.dto.PagosResponse;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesDTO;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesResponse;
import com.example.ms_registro.de.atenciones.model.RegistroAtenciones;
import com.example.ms_registro.de.atenciones.repository.RegistroAtencionesRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RegistroAtencionesServiceTest {


    @Mock
    private RegistroAtencionesRepository repository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @Mock
    private PagosClient pagosClient;

    @InjectMocks
    private RegistroAtencionesService service;

    private final String token = "Bearer token-de-prueba";

    @Test
    void deberiaCrearRegistroDeAtencionCorrectamente() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");
        dto.setNompaciente("Juan Pérez");
        dto.setRunmedico("22222222-2");
        dto.setNommedico("Dra. Soto");
        dto.setTotal(45000.0);
        dto.setIdPago(1);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHora(LocalTime.of(10, 30));
        dto.setTratamientoRealizado("Extración Molar");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pagos = new PagosResponse();
        pagos.setId(1L);
        pagos.setTotal(45000.0);
        pagos.setEstado("PAGADO");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(pagos);
        when(repository.save(any(RegistroAtenciones.class))).thenReturn(
            new RegistroAtenciones(
                1L,
                "Juan Pérez",
                "11111111-1",
                "Dra. Soto",
                "22222222-2",
                45000.0,
                1,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 30),
                "Extración Molar"
            )
        );

        // Act
        RegistroAtencionesResponse resultado = service.crear(dto, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getPaciente().getNombrePaciente());
        assertEquals("Dra. Soto", resultado.getMedico().getNombreMedico());
        assertEquals("PAGADO", resultado.getPago().getEstado());
        assertEquals("Extración Molar", resultado.getTratamientoRealizado());
        verify(repository).save(any(RegistroAtenciones.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiPacienteNoExiste() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("el paciente no existe", ex.getMessage());
        verify(repository, never()).save(any(RegistroAtenciones.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiMedicoNoExiste() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");
        dto.setRunmedico("22222222-2");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("El médico no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiPagoNoExiste() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");
        dto.setRunmedico("22222222-2");
        dto.setIdPago(1);

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("El pago no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaRetornarListaDeRegistros() {
        // Arrange
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pagos = new PagosResponse();
        pagos.setId(1L);
        pagos.setTotal(45000.0);
        pagos.setEstado("PAGADO");

        when(repository.findAll()).thenReturn(List.of(
            new RegistroAtenciones(
                1L,
                "Juan Pérez",
                "11111111-1",
                "Dra. Soto",
                "22222222-2",
                45000.0,
                1,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 30),
                "Extración Molar"
            )
        ));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(pagos);

        // Act
        List<RegistroAtencionesResponse> resultado = service.listar(token);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getPaciente().getNombrePaciente());
        verify(repository).findAll();
    }

    @Test
    void deberiaRetornarRegistroCuandoExiste() {
        // Arrange
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pagos = new PagosResponse();
        pagos.setId(1L);
        pagos.setEstado("PAGADO");

        when(repository.findById(1L)).thenReturn(Optional.of(
            new RegistroAtenciones(
                1L,
                "Juan Pérez",
                "11111111-1",
                "Dra. Soto",
                "22222222-2",
                45000.0,
                1,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 30),
                "Extración Molar"
            )
        ));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(pagos);

        // Act
        RegistroAtencionesResponse resultado = service.obtener(1L, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Extración Molar", resultado.getTratamientoRealizado());
        verify(repository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoRegistroNoExiste() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );

        assertEquals("Registro de atenciones no se encontro", ex.getMessage());
        verify(repository).findById(99L);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiPacienteNoExiste() {
    // Arrange
    RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
    dto.setRunpaciente("99999999-9");
    dto.setRunmedico("22222222-2");
    dto.setIdPago(1);

    when(pacienteClient.getPacienteClient("99999999-9", token)).thenReturn(null);

    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.actualizar(99L, dto, token)
    );

    assertEquals("el paciente no existe", ex.getMessage());
    verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiMedicoNoExiste() {
    // Arrange
    RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
    dto.setRunpaciente("11111111-1");
    dto.setRunmedico("99999999-9");
    dto.setIdPago(1);

    PacienteResponse paciente = new PacienteResponse();
    paciente.setRunPaciente("11111111-1");
    paciente.setNombrePaciente("Juan Pérez");

    when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
    when(medicoClient.getMedicoClient("99999999-9", token)).thenReturn(null);

    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.actualizar(99L, dto, token)
    );

    assertEquals("El médico no existe", ex.getMessage());
    verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiPagoNoExiste() {
    // Arrange
    RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
    dto.setRunpaciente("11111111-1");
    dto.setRunmedico("22222222-2");
    dto.setIdPago(1);

    PacienteResponse paciente = new PacienteResponse();
    paciente.setRunPaciente("11111111-1");
    paciente.setNombrePaciente("Juan Pérez");

    MedicoResponse medico = new MedicoResponse();
    medico.setRunMedico("22222222-2");
    medico.setNombreMedico("Dra. Soto");

    when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
    when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
    when(pagosClient.getPagosClient(1, token)).thenReturn(null);

    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.actualizar(99L, dto, token)
    );

    assertEquals("El pago no existe", ex.getMessage());
    verify(repository, never()).save(any());
    }

    @Test
    void deberiaActualizarRegistroCorrectamente() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");
        dto.setNompaciente("Juan Pérez");
        dto.setRunmedico("22222222-2");
        dto.setNommedico("Dra. Soto");
        dto.setTotal(45000.0);
        dto.setIdPago(1);
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHora(LocalTime.of(11, 0));
        dto.setTratamientoRealizado("Control");

        RegistroAtenciones existente = new RegistroAtenciones(
            1L,
            "Juan Pérez",
            "11111111-1",
            "Dra. Soto",
            "22222222-2",
            45000.0,
            1,
            LocalDate.of(2026, 6, 20),
            LocalTime.of(10, 30),
            "Extración Molar"
        );

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pagos = new PagosResponse();
        pagos.setId(1L);
        pagos.setEstado("PAGADO");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(pagos);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(RegistroAtenciones.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        RegistroAtencionesResponse resultado = service.actualizar(1L, dto, token);

        // Assert
        assertEquals(LocalDate.of(2026, 6, 25), resultado.getFecha());
        assertEquals(LocalTime.of(11, 0), resultado.getHora());
        assertEquals("Control", resultado.getTratamientoRealizado());
        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiRegistroNoExiste() {
        // Arrange
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setRunpaciente("11111111-1");
        dto.setRunmedico("22222222-2");
        dto.setIdPago(1);

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        PagosResponse pagos = new PagosResponse();
        pagos.setEstado("PAGADO");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(pagosClient.getPagosClient(1, token)).thenReturn(pagos);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
        );

        assertEquals("Registro de atenciones no encontrado", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaEliminarRegistroPorId() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act
        service.eliminar(1L);

        // Assert
        verify(repository).deleteById(1L);
    }
}