package com.example.ms_receta.medica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_receta.medica.client.MedicoClient;
import com.example.ms_receta.medica.dto.MedicoResponse;
import com.example.ms_receta.medica.dto.RecetaMedicaDTO;
import com.example.ms_receta.medica.dto.RecetaMedicaResponce;
import com.example.ms_receta.medica.model.RecetaMedica;
import com.example.ms_receta.medica.repository.RecetaMedicaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RecetaMedicaServiceTest {

    @Mock
    private RecetaMedicaRepository repository;

    @Mock
    private MedicoClient medicoClient;

    @InjectMocks
    private RecetaMedicaService service;

    private final String token = "Bearer token-de-prueba";

    @Test
    void deberiaCrearRecetaMedicaCorrectamente() {
        // Arrange
        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setRunMedico("22222222-2");
        dto.setNomMedico("Dra. Soto");
        dto.setNomMedicamento("Ibuprofeno");
        dto.setDiasTomarMedicamento(7);
        dto.setInicioReceta(LocalDate.of(2026, 6, 20));
        dto.setCantTomarDia(3);
        dto.setFirmaMedico("Firma Dra. Soto");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repository.save(any(RecetaMedica.class))).thenReturn(
            new RecetaMedica(
                1L,
                "Ibuprofeno",
                7,
                LocalDate.of(2026, 6, 20),
                "Dra. Soto",
                "22222222-2",
                3,
                "Firma Dra. Soto"
            )
        );

        // Act
        RecetaMedicaResponce resultado = service.crear(dto, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ibuprofeno", resultado.getNomMedicamento());
        assertEquals(7, resultado.getDiasTomarMedicamento());
        assertEquals("Dra. Soto", resultado.getMedico().getNombreMedico());
        verify(repository).save(any(RecetaMedica.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiMedicoNoExiste() {
        // Arrange
        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setRunMedico("99999999-9");

        when(medicoClient.getMedicoClient("99999999-9", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("Médico no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaRetornarListaDeRecetas() {
        // Arrange
        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(repository.findAll()).thenReturn(List.of(
            new RecetaMedica(
                1L,
                "Ibuprofeno",
                7,
                LocalDate.of(2026, 6, 20),
                "Dra. Soto",
                "22222222-2",
                3,
                "Firma Dra. Soto"
            )
        ));
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);

        // Act
        List<RecetaMedicaResponce> resultado = service.listar(token);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Ibuprofeno", resultado.get(0).getNomMedicamento());
        verify(repository).findAll();
    }

    @Test
    void deberiaRetornarRecetaCuandoExiste() {
        // Arrange
        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(repository.findById(1L)).thenReturn(Optional.of(
            new RecetaMedica(
                1L,
                "Ibuprofeno",
                7,
                LocalDate.of(2026, 6, 20),
                "Dra. Soto",
                "22222222-2",
                3,
                "Firma Dra. Soto"
            )
        ));
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);

        // Act
        RecetaMedicaResponce resultado = service.obtener(1L, token);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ibuprofeno", resultado.getNomMedicamento());
        verify(repository).findById(1L);
    }


    @Test
    void deberiaLanzarExcepcionCuandoRecetaNoExiste() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );

        assertEquals("Receta médica no encontrada", ex.getMessage());
        verify(repository).findById(99L);
    }

    @Test
    void deberiaActualizarRecetaCorrectamente() {
        // Arrange
        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setRunMedico("22222222-2");
        dto.setNomMedico("Dra. Soto");
        dto.setNomMedicamento("Paracetamol");
        dto.setDiasTomarMedicamento(5);
        dto.setInicioReceta(LocalDate.of(2026, 6, 25));
        dto.setCantTomarDia(2);
        dto.setFirmaMedico("Firma Dra. Soto");

        RecetaMedica existente = new RecetaMedica(
            1L,
            "Ibuprofeno",
            7,
            LocalDate.of(2026, 6, 20),
            "Dra. Soto",
            "22222222-2",
            3,
            "Firma Dra. Soto"
        );

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(RecetaMedica.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        RecetaMedicaResponce resultado = service.actualizar(1L, dto, token);

        // Assert
        assertEquals("Paracetamol", resultado.getNomMedicamento());
        assertEquals(5, resultado.getDiasTomarMedicamento());
        assertEquals(LocalDate.of(2026, 6, 25), resultado.getInicioReceta());
        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiMedicoNoExiste() {
        // Arrange
        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setRunMedico("99999999-9");

        when(medicoClient.getMedicoClient("99999999-9", token)).thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.actualizar(99L, dto, token)
        );

        assertEquals("Médico no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiRecetaNoExiste() {
        // Arrange
        RecetaMedicaDTO dto = new RecetaMedicaDTO();
        dto.setRunMedico("22222222-2");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");

        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(medico);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
        );

        assertEquals("Receta médica no encontrada", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaEliminarRecetaPorId() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act
        service.eliminar(1L);

        // Assert
        verify(repository).deleteById(1L);
    }
}