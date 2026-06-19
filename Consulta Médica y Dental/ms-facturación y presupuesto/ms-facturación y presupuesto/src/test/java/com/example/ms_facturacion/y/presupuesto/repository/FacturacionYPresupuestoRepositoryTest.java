package com.example.ms_facturacion.y.presupuesto.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_facturacion.y.presupuesto.model.FacturacionYPresupuesto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class FacturacionYPresupuestoRepositoryTest {

    @Autowired
    private FacturacionYPresupuestoRepository repository;

    @Test
    void debeGuardarFacturacionYPresupuesto() {
    FacturacionYPresupuesto facYpre = new FacturacionYPresupuesto(
        1L, 30.000, "paciente","11111111-1",
         "medico","22222222-2","tratamiento",
    8, "gestionPagos");

        FacturacionYPresupuesto guardado = repository.save(facYpre);

        assertNotNull(guardado.getId());
    assertEquals(30.000, guardado.getPresupuesto());

    assertEquals("paciente", guardado.getNombrePaciente());
    assertEquals("11111111-1", guardado.getRunPaciente());

    assertEquals("medico", guardado.getNombreMedico());
    assertEquals("1-2", guardado.getRunMedico());

    assertEquals("tratamiento", guardado.getTratamiento());
    assertEquals(8, guardado.getDiasDuracion());
    assertEquals("gestionPagos", guardado.getGestionPagos());
    }

    @Test
    void debeBuscarFacturacionYPresupuestoPorId() {
    FacturacionYPresupuesto facYpre = new FacturacionYPresupuesto( 1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos");
        
        FacturacionYPresupuesto guardado = repository.save(facYpre);

        Optional<FacturacionYPresupuesto> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
    assertEquals(30.000, resultado.get().getPresupuesto());

    assertEquals("paciente", resultado.get().getNombrePaciente());
    assertEquals("11111111-1", resultado.get().getRunPaciente());

    assertEquals("medico", resultado.get().getNombreMedico());
    assertEquals("1-2", resultado.get().getRunMedico());

    assertEquals("tratamiento", resultado.get().getTratamiento());
    assertEquals(8, resultado.get().getDiasDuracion());
    assertEquals("gestionPagos", resultado.get().getGestionPagos());
    }

    @Test
    void debeListarFacturacionYPresupuesto() {
        repository.save(new FacturacionYPresupuesto( 1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos"));
        repository.save(new FacturacionYPresupuesto( 1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos"));

        List<FacturacionYPresupuesto> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarFacturacionYPresupuesto() {
        FacturacionYPresupuesto facypre = new FacturacionYPresupuesto( 1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos");

        FacturacionYPresupuesto guardado = repository.save(facypre);

        repository.deleteById(guardado.getId());

        Optional<FacturacionYPresupuesto> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}

