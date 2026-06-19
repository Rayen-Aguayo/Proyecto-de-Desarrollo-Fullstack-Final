package com.example.ms_reserva.y.anular.hora.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_reserva.y.anular.hora.client.MedicoClient;
import com.example.ms_reserva.y.anular.hora.client.PacienteClient;
import com.example.ms_reserva.y.anular.hora.dto.MedicoResponse;
import com.example.ms_reserva.y.anular.hora.dto.PacienteResponse;
import com.example.ms_reserva.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reserva.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reserva.y.anular.hora.model.PedirHora;
import com.example.ms_reserva.y.anular.hora.repository.PedirHoraRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedirHoraServiceTest {

    @Mock
    private PedirHoraRepository repo;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @InjectMocks
    private PedirHoraService service;

    private final String token = "Bearer token-de-prueba";

    @Test
    void deberiaCrearReservaCorrectamente() {
        // Arrange
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

        PedirHora guardado = new PedirHora(1L, "11111111-1", "Juan Pérez", "22222222-2", "Dra. Soto",
                dto.getFecha(), dto.getHoraDeAtencion(), "Consulta dental");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repo.save(any(PedirHora.class))).thenReturn(guardado);

        // Act
        PedirHoraResponse resultado = service.crear(dto, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getPaciente().getNombrePaciente());
        assertEquals("Dra. Soto", resultado.getMedico().getNombreMedico());
        assertEquals("Consulta dental", resultado.getAtencion());
        verify(repo).save(any(PedirHora.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiPacienteNoExiste() {
        // Arrange
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("99999999-9");
        dto.setRunMedico("22222222-2");

        when(pacienteClient.getPacienteClient("99999999-9", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("el paciente no existe no se le puede reservar una hora", ex.getMessage());
        verify(repo, never()).save(any(PedirHora.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiMedicoNoExiste() {
        // Arrange
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setRunMedico("99999999-9");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("99999999-9", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("El médico no existe", ex.getMessage());
        verify(repo, never()).save(any(PedirHora.class));
    }

    @Test
    void deberiaRetornarListaDeReservas() {
        // Arrange
        PedirHora pedirHora = new PedirHora(1L, "11111111-1", "Juan Pérez", "22222222-2", "Dra. Soto",
                LocalDate.of(2026, 6, 20), LocalTime.of(10, 30), "Consulta dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(repo.findAll()).thenReturn(List.of(pedirHora));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);

        // Act
        List<PedirHoraResponse> resultado = service.listar(token);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getPaciente().getNombrePaciente());
        verify(repo).findAll();
    }

    @Test
    void deberiaRetornarReservaCuandoExiste() {
        // Arrange
        PedirHora pedirHora = new PedirHora(1L, "11111111-1", "Juan Pérez", "22222222-2", "Dra. Soto",
                LocalDate.of(2026, 6, 20), LocalTime.of(10, 30), "Consulta dental");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setNombreMedico("Dra. Soto");

        when(repo.findById(1L)).thenReturn(Optional.of(pedirHora));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);

        // Act
        PedirHoraResponse resultado = service.obtener(1L, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Consulta dental", resultado.getAtencion());
        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiPacienteNoExiste() {
    // Arrange
    PedirHoraDTO dto = new PedirHoraDTO();
    dto.setRunPaciente("99999999-9");
    dto.setRunMedico("22222222-2");

    when(pacienteClient.getPacienteClient("99999999-9", token)).thenReturn(null);

    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.actualizar(99L, dto, token)
    );

    assertEquals("el paciente no existe", ex.getMessage());
    verify(repo, never()).save(any(PedirHora.class));
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiMedicoNoExiste() {
    // Arrange
    PedirHoraDTO dto = new PedirHoraDTO();
    dto.setRunPaciente("11111111-1");
    dto.setRunMedico("99999999-9");

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
    verify(repo, never()).save(any(PedirHora.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoReservaNoExiste() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );

        assertEquals("no se a encontrado ninguna reserva de hora", ex.getMessage());
        verify(repo).findById(99L);
    }

    @Test
    void deberiaActualizarReservaCorrectamente() {
        // Arrange
        PedirHora existente = new PedirHora
            (1L, "11111111-1", "Juan Pérez", "22222222-2", "Dra. Soto",
            LocalDate.of(2026, 6, 20), LocalTime.of(10, 30), "Consulta dental");

        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHoraDeAtencion(LocalTime.of(11, 0));
        dto.setAtencion("Control");

        PacienteResponse paciente = new PacienteResponse();
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setNombreMedico("Dra. Soto");

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(PedirHora.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PedirHoraResponse resultado = service.actualizar(1L, dto, token);

        // Assert
        assertEquals(1L, resultado.getId());
        assertEquals(LocalDate.of(2026, 6, 25), resultado.getFecha());
        assertEquals(LocalTime.of(11, 0), resultado.getHoraDeAtencion());
        verify(repo).findById(1L);
        verify(repo).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiReservaNoExiste() {
        // Arrange
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHoraDeAtencion(LocalTime.of(11, 0));
        dto.setAtencion("Control");

        PacienteResponse paciente = new PacienteResponse();
        MedicoResponse medico = new MedicoResponse();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(paciente);
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
        );

        assertEquals("reserva de hora no encontrado", ex.getMessage());
        verify(repo, never()).save(any(PedirHora.class));
    }

    @Test
    void deberiaEliminarReservaPorId() {
        // Arrange
        doNothing().when(repo).deleteById(1L);

        // Act
        service.eliminar(1L);

        // Assert
        verify(repo).deleteById(1L);
    }
}

