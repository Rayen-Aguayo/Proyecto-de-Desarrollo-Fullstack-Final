package com.example.ms_reserva.y.anular.hora.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_reserva.y.anular.hora.model.PedirHora;

@DataJpaTest
@ActiveProfiles("test")
class PedirHoraRepositoryTest {

    @Autowired
    private PedirHoraRepository repository;

    private PedirHora horaEjemplo() {
        return new PedirHora(
            null,
            "11111111-1",
            "Juan Pérez",
            "22222222-2",
            "Dra. Soto",
            LocalDate.of(2026, 6, 20),
            LocalTime.of(10, 30),
            "Consulta dental"
        );
    }

    @Test
    void debeGuardarReservaHora() {
        PedirHora guardada = repository.save(horaEjemplo());

        assertNotNull(guardada.getId());
        assertEquals("11111111-1", guardada.getRunPaciente());
        assertEquals("Juan Pérez", guardada.getNombrePaciente());
        assertEquals("Consulta dental", guardada.getAtencion());
    }

    @Test
    void debeBuscarReservaPorId() {
        PedirHora guardada = repository.save(horaEjemplo());

        Optional<PedirHora> resultado = repository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals("11111111-1", resultado.get().getRunPaciente());
        assertEquals("Dra. Soto", resultado.get().getNombreMedico());
    }

    @Test
    void debeListarReservas() {
        repository.save(horaEjemplo());
        repository.save(new PedirHora(
            null,
            "33333333-3",
            "María López",
            "44444444-4",
            "Dr. Rojas",
            LocalDate.of(2026, 7, 1),
            LocalTime.of(9, 0),
            "Control dental"
        ));

        List<PedirHora> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarReserva() {
        PedirHora guardada = repository.save(horaEjemplo());

        repository.deleteById(guardada.getId());

        Optional<PedirHora> resultado = repository.findById(guardada.getId());
        assertFalse(resultado.isPresent());
    }
}
