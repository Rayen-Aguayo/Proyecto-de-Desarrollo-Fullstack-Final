package com.example.ms_medico.service;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.repository.MedicoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @Mock
    private MedicoRepository repo;

    @InjectMocks
    private MedicoService service;

    @Test
    void deberiaRetornarMedicoCuandoExiste() {
        Medico medico = new Medico("22222222-2","medico", 28, "123456789",
            "especialidad","firmaMedico"
        );

        when(repo.findById("22222222-2")).thenReturn(Optional.of(medico));

        Medico resultado = service.obtener("22222222-2");

        assertNotNull(resultado);
        assertEquals("22222222-2", resultado.getRunMedico());
        assertEquals("medico", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());

        verify(repo).findById("22222222-2");
    }

    @Test
    void deberiaLanzarExcepcionCuandoMedicoNoExiste() {
        when(repo.findById("9999999-9")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener("9999999-9")
        );

        assertEquals("Medico no encontrado", ex.getMessage());
        verify(repo).findById("9999999-9");
    }

    @Test
    void deberiaRetornarListaMedicos() {
        Medico medico = new Medico("22222222-2","medico", 28, "123456789",
            "especialidad","firmaMedico"
        );
        when(repo.findAll()).thenReturn(List.of(medico));

        List<Medico> resultado = service.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("medico", resultado.get(0).getNombreMedico());
        verify(repo).findAll();
    }

    @Test
    void deberiaCrearMedicoCorrectamente() {
        MedicoDTO dto = new MedicoDTO();
        dto.getNombreMedico();
        dto.getEspecialidad();
        dto.getEdad();
        dto.getNroTelefono();
        dto.getFirmaMedico();

        Medico Guardado = new Medico("22222222-2","medico", 28, "123456789",
            "especialidad","firmaMedico"
        );
        when(repo.save(any(Medico.class))).thenReturn(Guardado);

        Medico resultado = service.crear(dto);

        assertNotNull(resultado);
        assertEquals("22222222-2", resultado.getRunMedico());
        assertEquals("medico", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());
        verify(repo).save(any(Medico.class));
    }

    @Test
    void deberiaActualizarMedicoCorrectamente() {
        Medico existente = new Medico("22222222-2","medico nuevo", 28, "123456789",
            "especialidad","firmaMedico"
        );

        MedicoDTO dto = new MedicoDTO();
        dto.setNombreMedico("medico nuevo");
        dto.setEspecialidad("especialidad");
        dto.setEdad(28);
        dto.setNroTelefono("123456789");
        dto.setFirmaMedico("firmaMedico");

        when(repo.findById("22222222-2")).thenReturn(Optional.of(existente));
        when(repo.save(any(Medico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medico resultado = service.actualizar("22222222-2", dto);

        assertEquals("22222222-2", resultado.getRunMedico());
        assertEquals("medico nuevo", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());
        verify(repo).findById("1-2");
        verify(repo).save(existente);
    }

    @Test
    void deberiaEliminarMedicoPorId() {
        doNothing().when(repo).deleteById("22222222-2");

        service.eliminar("22222222-2");

        verify(repo).deleteById("22222222-2");
    }
}

