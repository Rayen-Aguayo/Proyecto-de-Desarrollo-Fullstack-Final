package com.example.ms_registro.de.atenciones.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.ms_registro.de.atenciones.model.RegistroAtenciones;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RegistroAtencionesRepositoryTest {

    @Autowired
    private RegistroAtencionesRepository repository;

    @Test
    void debeGuardarRegistroAtencion() {
        RegistroAtenciones registro = new RegistroAtenciones(
                null,
                "Juan Pérez",
                "11111111-1",
                "Dra. Soto",
                "22222222-2",
                50000.0,
                1,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 30),
                "Limpieza dental"
        );

        RegistroAtenciones guardado = repository.save(registro);

        assertNotNull(guardado.getId());
        assertEquals("Juan Pérez", guardado.getNompaciente());
        assertEquals("Dra. Soto", guardado.getNommedico());
        assertEquals("Limpieza dental", guardado.getTratamientoRealizado());
        assertEquals(50000.0, guardado.getTotal());
    }

    @Test
    void debeBuscarRegistroPorId() {
        RegistroAtenciones registro = new RegistroAtenciones(
                null,
                "María López",
                "33333333-3",
                "Dr. Rojas",
                "44444444-4",
                35000.0,
                2,
                LocalDate.of(2026, 7, 1),
                LocalTime.of(9, 0),
                "Control dental"
        );
        RegistroAtenciones guardado = repository.save(registro);

        Optional<RegistroAtenciones> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("María López", resultado.get().getNompaciente());
        assertEquals("Dr. Rojas", resultado.get().getNommedico());
        assertEquals("Control dental", resultado.get().getTratamientoRealizado());
    }

    @Test
    void debeListarRegistros() {
        repository.save(new RegistroAtenciones(
                null, "Pedro Díaz", "55555555-5",
                "Dra. Fuentes", "66666666-6",
                60000.0, 3,
                LocalDate.of(2026, 6, 22),
                LocalTime.of(11, 0),
                "Extracción dental"
        ));
        repository.save(new RegistroAtenciones(
                null, "Ana Torres", "77777777-7",
                "Dr. Muñoz", "88888888-8",
                45000.0, 4,
                LocalDate.of(2026, 6, 23),
                LocalTime.of(15, 30),
                "Consulta médica"
        ));

        List<RegistroAtenciones> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarRegistro() {
        RegistroAtenciones registro = new RegistroAtenciones(
                null, "Carlos Vidal", "99999999-9",
                "Dra. Castro", "10101010-1",
                70000.0, 5,
                LocalDate.of(2026, 8, 5),
                LocalTime.of(16, 0),
                "Control médico"
        );
        RegistroAtenciones guardado = repository.save(registro);

        repository.deleteById(guardado.getId());

        Optional<RegistroAtenciones> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}