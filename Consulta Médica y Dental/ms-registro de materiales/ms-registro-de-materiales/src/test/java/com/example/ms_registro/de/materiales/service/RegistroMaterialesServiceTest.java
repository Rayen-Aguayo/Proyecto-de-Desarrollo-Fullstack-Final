package com.example.ms_registro.de.materiales.service;

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

import com.example.ms_registro.de.materiales.dto.RegistroMaterialesDTO;
import com.example.ms_registro.de.materiales.model.RegistroMateriales;
import com.example.ms_registro.de.materiales.repository.RegistroMaterialesRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RegistroMaterialesServiceTest {

    @Mock
    private RegistroMaterialesRepository repository;

    @InjectMocks
    private RegistroMaterialesService service;

    @Test
    void deberiaCrearRegistroDeAtencionCorrectamente() {
        //Arrange
        RegistroMaterialesDTO dto = new RegistroMaterialesDTO();
        dto.setCantidadProductos(10);
        dto.setNombresProductos("Guantes, Mascarillas");
        dto.setFechaCaducidadProductos(LocalDate.of(2027, 1, 1));

        when(repository.save(any(RegistroMateriales.class))).thenReturn(
            new RegistroMateriales(
                1L, 10,
                "Guantes, Mascarillas",
                LocalDate.of(2027, 1, 1)
            )
        );

        RegistroMateriales resultado = service.crear(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10, resultado.getCantidadProductos());
        assertEquals("Guantes, Mascarillas", resultado.getNombresProductos());
        assertEquals(LocalDate.of(2027, 1, 1), resultado.getFechaCaducidadProductos());
        verify(repository).save(any(RegistroMateriales.class));
    }

    @Test
    void deberiaRetornarListaDeMateriales() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(
            new RegistroMateriales(
                1L, 10,
                "Guantes, Mascarillas",
                LocalDate.of(2027, 1, 1)
            )
        )
    );

        // Act
        List<RegistroMateriales> resultado = service.listar();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Guantes, Mascarillas", resultado.get(0).getNombresProductos());
        verify(repository).findAll();
    }

    @Test
    void deberiaRetornarMaterialesCuandoExiste() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(
            new RegistroMateriales(
                1L, 10,
                "Guantes, Mascarillas",
                LocalDate.of(2027, 1, 1)
            )
        )
    );

        //Act
        RegistroMateriales resultado = service.obtener(1L);

        //Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Guantes, Mascarillas", resultado.getNombresProductos());
        verify(repository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionesCuandoMaterialesNoExiste() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        //Act + Assert
        EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.obtener(99L)
        );

        assertEquals("registro de materiales no encontrado", ex.getMessage());
        verify(repository).findById(99L);
    }

    @Test
    void deberiaActualizarMaterialCorrectamente() {
        //Arrange
        RegistroMaterialesDTO dto = new RegistroMaterialesDTO();
        dto.setCantidadProductos(20);
        dto.setNombresProductos("Jeringas");
        dto.setFechaCaducidadProductos(LocalDate.of(2028, 6, 1));

        RegistroMateriales existente = new RegistroMateriales(
            1L, 10,
            "Guantes, Mascarillas",
            LocalDate.of(2027, 1, 1)
        );

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(RegistroMateriales.class))).thenAnswer(inv -> inv.getArgument(0));
        
        // Act
        RegistroMateriales resultado = service.actualizar(1L, dto);

        // Assert
        assertEquals(20, resultado.getCantidadProductos());
        assertEquals("Jeringas", resultado.getNombresProductos());
        assertEquals(LocalDate.of(2028, 6, 1), resultado.getFechaCaducidadProductos());
        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiMaterialNoExiste() {
        // Arrange
        RegistroMaterialesDTO dto = new RegistroMaterialesDTO();
        dto.setCantidadProductos(20);
        dto.setNombresProductos("Jeringas");
        dto.setFechaCaducidadProductos(LocalDate.of(2028, 6, 1));

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto)
        );

        assertEquals("registro de materiales no encontrado", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaEliminarRegistroPorId() {
        doNothing().when(repository).deleteById(1L);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}