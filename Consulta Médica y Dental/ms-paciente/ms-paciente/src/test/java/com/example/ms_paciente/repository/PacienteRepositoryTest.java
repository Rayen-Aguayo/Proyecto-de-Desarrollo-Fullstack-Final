package com.example.ms_paciente.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import com.example.ms_paciente.model.Paciente;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository repository;

    @Test
    void debeGuardarPaciente() {
        Paciente paciente = new Paciente(
            "11111111-1", 
            "paciente", 
            "datos del paciente",
            28,
            "alergias"
            ,"enfermedad",
            "medicamento"
            ,"123456789"
        );

        Paciente guardado = repository.save(paciente);

        assertNotNull(guardado.getRunPaciente());

        assertEquals("11111111-1", guardado.getRunPaciente());
        assertEquals("paciente", guardado.getNombrePaciente());
        assertEquals(28, guardado.getEdadPaciente());
        assertEquals("alergias", guardado.getAlergias());
        assertEquals("enfermedad", guardado.getEnfermedad());
        assertEquals("medicamento", guardado.getQueMedicamentoEstaTomando());
        assertEquals("123456789", guardado.getNroTelefono());

    }

    @Test
    void debeBuscarPacientePorId() {
        Paciente paciente = new Paciente(
            "11111111-1", 
            "paciente", 
            "datos del paciente",
            28,
            "alergias",
            "enfermedad",
            "medicamento",
            "123456789"
        );
        Paciente guardado = repository.save(paciente);

        Optional<Paciente> resultado = repository.findById(guardado.getRunPaciente());

        assertEquals("11111111-1", resultado.get().getRunPaciente());
        assertEquals("paciente", resultado.get().getNombrePaciente());
        assertEquals(28, resultado.get().getEdadPaciente());
        assertEquals("alergias", resultado.get().getAlergias());
        assertEquals("enfermedad", resultado.get().getEnfermedad());
        assertEquals("medicamento", resultado.get().getQueMedicamentoEstaTomando());
        assertEquals("123456789", resultado.get().getNroTelefono());
    }

    @Test
    void debeListarPacientes() {
        repository.save(new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        ));
        repository.save(new Paciente("11111111-2", "paciente 2", "datos del paciente",
            22,"alergias","enfermedad",
            "medicamento","987654321"
        ));

        List<Paciente> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarPaciente() {
        Paciente paciente = new Paciente("11111111-1", "paciente", "datos del paciente",
            28,"alergias","enfermedad",
            "medicamento","123456789"
        );
        Paciente guardado = repository.save(paciente);

        repository.deleteById(guardado.getRunPaciente());

        Optional<Paciente> resultado = repository.findById(guardado.getRunPaciente());
        assertFalse(resultado.isPresent());
    }
}