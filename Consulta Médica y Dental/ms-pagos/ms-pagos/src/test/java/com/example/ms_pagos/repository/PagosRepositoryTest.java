package com.example.ms_pagos.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_pagos.model.Pagos;

@DataJpaTest
@ActiveProfiles("test")
class PagosRepositoryTest {

    @Autowired
    private PagosRepository repository;

    private Pagos pagoEjemplo() {
        return new Pagos(
            null, 
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
    }

    @Test
    void debeGuardarPago() {
        Pagos guardado = repository.save(pagoEjemplo());

        assertNotNull(guardado.getId());
        assertEquals("12345678-9", guardado.getRunPaciente());
        assertEquals("Juan Pérez", guardado.getNombrePaciente());
        assertEquals("Pagado", guardado.getEstado());
        assertEquals(11900.0, guardado.getTotal());
    }

    @Test
    void debeBuscarPagoPorId() {
        Pagos guardado = repository.save(pagoEjemplo());

        Optional<Pagos> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("12345678-9", resultado.get().getRunPaciente());
        assertEquals("Tarjeta", resultado.get().getMetodoPago());
    }

    @Test
    void debeListarPagos() {
        repository.save(pagoEjemplo());
        repository.save(new Pagos(
            null, 
            "98765432-1", 
            "María López",
            LocalDate.of(2026, 7, 15), 
            LocalTime.of(14, 30),
            "Efectivo", 
            1002, 
            "RF-002", 
            20000.0, 
            3800.0, 
            23800.0, 
            "Pendiente"
        )
    );

        List<Pagos> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarPago() {
        Pagos guardado = repository.save(pagoEjemplo());

        repository.deleteById(guardado.getId());

        Optional<Pagos> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}