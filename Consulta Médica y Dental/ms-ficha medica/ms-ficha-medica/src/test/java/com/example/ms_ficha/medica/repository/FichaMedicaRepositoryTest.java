package com.example.ms_ficha.medica.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_ficha.medica.model.FichaMedica;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class FichaMedicaRepositoryTest {

    @Autowired
    private FichaMedicaRepository repository;

    @Test
    void debeGuardarFichaMedica() {
        FichaMedica fichaMedica = new FichaMedica(
                null, 
                "11111111-1", 
                "paciente",
                "22222222-2", 
                "medico", 
                "procedimiento",
                "queMedicamentoEstaTomando", 
                "enfermedad",
                "alergias", 
                "odontograma"
            );

        FichaMedica guardado = repository.save(fichaMedica);

        assertNotNull(guardado.getId());
        assertEquals("11111111-1", guardado.getRunPaciente());
        assertEquals("paciente", guardado.getNombrePaciente());
        assertEquals("22222222-2", guardado.getRunMedico());
        assertEquals("medico", guardado.getNombreMedico());
        assertEquals("procedimiento", guardado.getProcedimiento());
        assertEquals("queMedicamentoEstaTomando", guardado.getQueMedicamentoEstaTomando());
        assertEquals("enfermedad", guardado.getEnfermedad());
        assertEquals("alergias", guardado.getAlergias());
        assertEquals("odontograma", guardado.getOdontograma());
    }

    @Test
    void debeBuscarFichaMedicaPorId() {
        FichaMedica fichaMedica = new FichaMedica(
                null, 
                "11111111-1", 
                "paciente",
                "22222222-2", 
                "medico", 
                "procedimiento",
                "queMedicamentoEstaTomando", 
                "enfermedad",
                "alergias", 
                "odontograma"
            );

        FichaMedica guardado = repository.save(fichaMedica);

        Optional<FichaMedica> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("11111111-1", resultado.get().getRunPaciente());
        assertEquals("paciente", resultado.get().getNombrePaciente());
        assertEquals("22222222-2", resultado.get().getRunMedico());
        assertEquals("medico", resultado.get().getNombreMedico());
        assertEquals("procedimiento", resultado.get().getProcedimiento());
        assertEquals("queMedicamentoEstaTomando", resultado.get().getQueMedicamentoEstaTomando());
        assertEquals("enfermedad", resultado.get().getEnfermedad());
        assertEquals("alergias", resultado.get().getAlergias());
        assertEquals("odontograma", resultado.get().getOdontograma());
    }

    @Test
    void debeListarFichaMedica() {
        repository.save(new FichaMedica(
                null, 
                "11111111-1", 
                "paciente",
                "22222222-2", 
                "medico", 
                "procedimiento",
                "queMedicamentoEstaTomando", 
                "enfermedad",
                "alergias", 
                "odontograma"
            )
        );

        repository.save(new FichaMedica(
                null, 
                "33333333-3", 
                "paciente2",
                "44444444-4", 
                "medico2", 
                "procedimiento2",
                "queMedicamentoEstaTomando2", 
                "enfermedad2",
                "alergias2", 
                "odontograma2"
            )
        );

        List<FichaMedica> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarFichaMedica() {
        // CORRECCIÓN 8: id debe ser null para que JPA lo genere
        FichaMedica fichaMedica = new FichaMedica(
                null, 
                "11111111-1", 
                "paciente",
                "22222222-2", 
                "medico", 
                "procedimiento",
                "queMedicamentoEstaTomando", 
                "enfermedad",
                "alergias", 
                "odontograma"
            );

        FichaMedica guardado = repository.save(fichaMedica);

        repository.deleteById(guardado.getId());

        Optional<FichaMedica> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}