package com.example.ms_opinion.del.paciente.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_opinion.del.paciente.model.OpinionPaciente;


@DataJpaTest
@ActiveProfiles("test")
public class OpinionPacienteRepositoryTest {

    @Autowired
    private OpinionPacienteRepository repository;

    private OpinionPaciente opinion() {
        return new OpinionPaciente(
            null,
            "11111111-1",
            "22222222-2",
            "Dra. Soto",
            8,
            "muy amable",
            "bastante claro",
            "comentario",
            8
        );
    }

    @Test
    void debeGuardarOpinionPaciente() {
        OpinionPaciente guardada = repository.save(opinion());

        assertNotNull(guardada.getId());
        assertEquals("11111111-1", guardada.getRunPaciente());
    }

    @Test
    void debeBuscarOpinionPacientePorId() {
        OpinionPaciente guardada = repository.save(opinion());

        Optional<OpinionPaciente> resultado = repository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals("11111111-1", resultado.get().getRunPaciente());
        assertEquals("Dra. Soto", resultado.get().getNombreMedico());
    }

    @Test
    void debeListarOpinionPaciente() {
        repository.save(opinion());
        repository.save(new OpinionPaciente(
            null,
            "11111111-1",
            "22222222-2",
            "Dra. Soto",
            8,
            "muy amable",
            "bastante claro",
            "comentario",
            8
        ));

        List<OpinionPaciente> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarOpinionPaciente() {
        OpinionPaciente guardada = repository.save(opinion());

        repository.deleteById(guardada.getId());

        Optional<OpinionPaciente> resultado = repository.findById(guardada.getId());
        assertFalse(resultado.isPresent());
    }
}

