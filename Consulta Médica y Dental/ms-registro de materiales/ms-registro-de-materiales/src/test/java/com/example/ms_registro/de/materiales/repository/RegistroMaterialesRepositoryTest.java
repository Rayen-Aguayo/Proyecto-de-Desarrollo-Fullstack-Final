package com.example.ms_registro.de.materiales.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_registro.de.materiales.model.RegistroMateriales;

@DataJpaTest
@ActiveProfiles("test")
class RegistroMaterialesRepositoryTest {

    @Autowired
    private RegistroMaterialesRepository repository;

    private RegistroMateriales materialEjemplo() {
        return new RegistroMateriales(
            null, 10,
            "Guantes, Mascarillas",
            LocalDate.of(2027, 1, 1)
        );
    }

    @Test
    void debeGuardarRegistroMateriales() {
        RegistroMateriales guardado = repository.save(materialEjemplo());

        assertNotNull(guardado.getId());
        assertEquals(10, guardado.getCantidadProductos());
        assertEquals("Guantes, Mascarillas", guardado.getNombresProductos());
        assertEquals(LocalDate.of(2027, 1, 1), guardado.getFechaCaducidadProductos());
    }

    @Test
    void debeBuscarMaterialPorId() {
        RegistroMateriales guardado = repository.save(materialEjemplo());

        Optional<RegistroMateriales> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Guantes, Mascarillas", resultado.get().getNombresProductos());
        assertEquals(10, resultado.get().getCantidadProductos());
    }

    @Test
    void debeListarMateriales() {
        repository.save(materialEjemplo());
        repository.save(new RegistroMateriales(
            null, 20,
            "Jeringas",
            LocalDate.of(2028, 6, 1)
        ));

        List<RegistroMateriales> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarMaterial() {
        RegistroMateriales guardado = repository.save(materialEjemplo());

        repository.deleteById(guardado.getId());

        Optional<RegistroMateriales> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}
