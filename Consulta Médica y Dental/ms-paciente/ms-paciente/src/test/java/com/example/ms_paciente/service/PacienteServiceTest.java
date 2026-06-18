package com.example.ms_paciente.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_paciente.dto.PacienteDTO;
import com.example.ms_paciente.model.Paciente;
import com.example.ms_paciente.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private PacienteRepository repo;

    @InjectMocks
    private PacienteService service;

    @Test
    void deberiaRetornarPacienteCuandoExiste() {
        Paciente paciente = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );
        when(repo.findById("11111111-1")).thenReturn(Optional.of(paciente));

        Paciente resultado = service.obtener("11111111-1");

        assertNotNull(resultado);
        assertEquals("11111111-1", resultado.getRunPaciente());
        assertEquals("paciente", resultado.getNombrePaciente());
        assertEquals(28, resultado.getEdadPaciente());
        assertEquals("alergias", resultado.getAlergias());
        assertEquals("enfermedad", resultado.getEnfermedad());
        assertEquals("medicamento", resultado.getQueMedicamentoEstaTomando());
        assertEquals("123456789", resultado.getNroTelefono());
        verify(repo).findById("11111111-1");
    }

    @Test
    void deberiaLanzarExcepcionCuandoPacienteNoExiste() {
        when(repo.findById("11111111-9")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener("11111111-9")
        );

        assertEquals("paciente no encontrado", ex.getMessage());
        verify(repo).findById("11111111-9");
    }

    @Test
    void deberiaRetornarListaPacientes() {
        Paciente paciente = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );
        when(repo.findAll()).thenReturn(List.of(paciente));

        List<Paciente> resultado = service.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("paciente", resultado.get(0).getNombrePaciente());
        verify(repo).findAll();
    }

    @Test
    void deberiaCrearPacienteCorrectamente() {
        PacienteDTO dto = new PacienteDTO();
        
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("paciente");
        dto.setDatosDelPaciente("datos del paciente");
        dto.setEdadPaciente(28);
        dto.setAlergias("alergias"); 
        dto.setEnfermedad("enfermedad"); 
        dto.setQueMedicamentoEstaTomando("medicamento"); 
        dto.setNroTelefono("123456789");

        Paciente guardado = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );
        when(repo.save(any(Paciente.class))).thenReturn(guardado);

        Paciente resultado = service.crear(dto);

        assertNotNull(resultado);
        assertEquals("11111111-1", resultado.getRunPaciente());
        assertEquals("paciente", resultado.getNombrePaciente());
        assertEquals(28, resultado.getEdadPaciente());
        assertEquals("alergias", resultado.getAlergias());
        assertEquals("enfermedad", resultado.getEnfermedad());
        assertEquals("medicamento", resultado.getQueMedicamentoEstaTomando());
        assertEquals("123456789", resultado.getNroTelefono());
        verify(repo).save(any(Paciente.class));
    }

    @Test
    void deberiaActualizarPacienteCorrectamente() {
        Paciente existente = new Paciente("11111111-1", "paciente nuevo", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );

        PacienteDTO dto = new PacienteDTO();
        dto.setNombrePaciente("Paciente nuevo");
        dto.setDatosDelPaciente("datos del paciente");
        dto.setEdadPaciente(28);
        dto.setAlergias("alergias"); 
        dto.setEnfermedad("enfermedad"); 
        dto.setQueMedicamentoEstaTomando("medicamento"); 
        dto.setNroTelefono("123456789");

        when(repo.findById("11111111-1")).thenReturn(Optional.of(existente));
        when(repo.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Paciente resultado = service.actualizar("11111111-1", dto);

        assertEquals("11111111-1", resultado.getRunPaciente());
        assertEquals("paciente", resultado.getNombrePaciente());
        assertEquals(28, resultado.getEdadPaciente());
        assertEquals("alergias", resultado.getAlergias());
        assertEquals("enfermedad", resultado.getEnfermedad());
        assertEquals("medicamento", resultado.getQueMedicamentoEstaTomando());
        assertEquals("123456789", resultado.getNroTelefono());
        verify(repo).findById("11111111-1");
        verify(repo).save(existente);
    }

    @Test
    void deberiaEliminarPacientePorId() {
        doNothing().when(repo).deleteById("11111111-1");

        service.eliminar("11111111-1");

        verify(repo).deleteById("11111111-1");
    }
}


