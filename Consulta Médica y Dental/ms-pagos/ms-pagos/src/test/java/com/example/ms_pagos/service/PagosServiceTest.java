package com.example.ms_pagos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.example.ms_pagos.client.PacienteClient;
import com.example.ms_pagos.dto.PacienteResponse;
import com.example.ms_pagos.dto.PagosDTO;
import com.example.ms_pagos.dto.PagosResponse;
import com.example.ms_pagos.model.Pagos;
import com.example.ms_pagos.repository.PagosRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PagosServiceTest {

    @Mock
    private PagosRepository pagosRepository;

    @Mock
    private PacienteClient pacienteClient;

    @InjectMocks
    private PagosService pagosService;

    private final String TOKEN = "Bearer test-token";

    private PacienteResponse pacienteMock() {
        PacienteResponse p = new PacienteResponse();
        p.setRunPaciente("12345678-9");
        p.setNombrePaciente("Juan Pérez");
        return p;
    }

    private PagosDTO dtoCorrecto() {
        PagosDTO dto = new PagosDTO();
        dto.setRunPaciente("12345678-9");
        dto.setNombrePaciente("Juan Pérez");
        dto.setFecha(LocalDate.of(2026, 6, 1));
        dto.setHora(LocalTime.of(10, 0));
        dto.setMetodoPago("Tarjeta");
        dto.setNroBoleta(1001);
        dto.setRegistroFacturacion("RF-001");
        dto.setNeto(10000.0);
        dto.setIva(1900.0);
        dto.setTotal(11900.0);
        dto.setEstado("Pagado");
        return dto;
    }

    @Test
    void debeCrearPagoCorrectamente() {
        PagosDTO dto = dtoCorrecto();
        Pagos pagoGuardado = new Pagos(
            1L, 
            "12345678-9", 
            "Juan Pérez",
            LocalDate.of(2026, 6, 1), 
            LocalTime.of(10, 0),
            "Tarjeta", 
            1001, 
            "RF-001", 
            10000.0, 
            1900.0, 
            11900.0, 
            "Pagado"
        );

        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(pacienteMock());
        when(pagosRepository.save(any(Pagos.class))).thenReturn(pagoGuardado);

        PagosResponse resultado = pagosService.crear(dto, TOKEN);

        assertNotNull(resultado);
        assertEquals("Pagado", resultado.getEstado());
        assertEquals(11900.0, resultado.getTotal());
        verify(pagosRepository).save(any(Pagos.class));
    }

    @Test
    void debeLanzarExcepcionAlCrearSiPacienteNoExiste() {
        PagosDTO dto = dtoCorrecto();
        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagosService.crear(dto, TOKEN));

        assertEquals("El paciente no existe", ex.getMessage());
        verify(pagosRepository, never()).save(any());
    }

    @Test
    void debeListarPagos() {
        Pagos pago = new Pagos(
            1L, 
            "12345678-9", 
            "Juan Pérez",
            LocalDate.of(2026, 6, 1), 
            LocalTime.of(10, 0),
            "Tarjeta", 
            1001, 
            "RF-001", 
            10000.0, 
            1900.0, 
            11900.0, 
            "Pagado"
        );

        when(pagosRepository.findAll()).thenReturn(List.of(pago));
        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(pacienteMock());

        List<PagosResponse> resultado = pagosService.listar(TOKEN);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Pagado", resultado.get(0).getEstado());
        verify(pagosRepository).findAll();
    }

    @Test
    void debeRetornarListaVaciaSiNoHayPagos() {
        when(pagosRepository.findAll()).thenReturn(List.of());

        List<PagosResponse> resultado = pagosService.listar(TOKEN);

        assertTrue(resultado.isEmpty());
        verify(pagosRepository).findAll();
    }

    @Test
    void debeObtenerPagoPorId() {
        Pagos pago = new Pagos(
            1L, 
            "12345678-9", 
            "Juan Pérez",
            LocalDate.of(2026, 6, 1), 
            LocalTime.of(10, 0),
            "Tarjeta", 
            1001, 
            "RF-001", 
            10000.0, 
            1900.0, 
            11900.0, 
            "Pagado"
        );

        when(pagosRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(pacienteMock());

        PagosResponse resultado = pagosService.obtener(1L, TOKEN);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pagado", resultado.getEstado());
        verify(pagosRepository).findById(1L);
    }

    @Test
    void debeLanzarExcepcionSiPagoNoExiste() {
        when(pagosRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> pagosService.obtener(99L, TOKEN));

        assertEquals("el pago no existe", ex.getMessage());
        verify(pagosRepository).findById(99L);
    }

    @Test
    void debeActualizarEstadoDePago() {
        PagosDTO dto = dtoCorrecto();
        dto.setEstado("Anulado");

        Pagos pagoExistente = new Pagos(
            1L, 
            "12345678-9", 
            "Juan Pérez",
            LocalDate.of(2026, 6, 1), 
            LocalTime.of(10, 0),
            "Tarjeta", 
            1001, 
            "RF-001", 
            10000.0, 
            1900.0, 
            11900.0, 
            "Pagado"
        );

        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(pacienteMock());
        when(pagosRepository.findById(1L)).thenReturn(Optional.of(pagoExistente));
        when(pagosRepository.save(any(Pagos.class))).thenAnswer(inv -> inv.getArgument(0));

        PagosResponse resultado = pagosService.actualizar(1L, dto, TOKEN);

        assertNotNull(resultado);
        assertEquals("Anulado", resultado.getEstado());
        verify(pagosRepository).findById(1L);
        verify(pagosRepository).save(pagoExistente);
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiPacienteNoExiste() {
        PagosDTO dto = dtoCorrecto();
        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagosService.actualizar(1L, dto, TOKEN));

        assertEquals("El paciente no existe", ex.getMessage());
        verify(pagosRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiPagoNoExiste() {
        PagosDTO dto = dtoCorrecto();
        when(pacienteClient.getPacienteClient("12345678-9", TOKEN)).thenReturn(pacienteMock());
        when(pagosRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> pagosService.actualizar(99L, dto, TOKEN));

        assertEquals("El pago no existe", ex.getMessage());
        verify(pagosRepository, never()).save(any());
    }
}
