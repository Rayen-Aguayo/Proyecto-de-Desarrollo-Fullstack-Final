package com.example.ms_medico.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_medico.model.Medico;


@DataJpaTest
@ActiveProfiles("test")
public class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository repository;

    @Test
    void debeGuardarMedico() {
        Medico medico = new Medico(
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );

        Medico guardado = repository.save(medico);

        assertNotNull(guardado.getRunMedico());

        assertEquals("11111111-1", guardado.getRunMedico());
        assertEquals("paciente", guardado.getNombreMedico());
        assertEquals(28, guardado.getEdad());
        assertEquals("987654321", guardado.getNroTelefono());
        assertEquals("cirujano", guardado.getEspecialidad());
        assertEquals("firma-soto", guardado.getFirmaMedico());
    }

    @Test
    void debeBuscarMedicoPorId() {
        Medico medico = new Medico(
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );
        Medico guardado = repository.save(medico);

        Optional<Medico> resultado = repository.findById(guardado.getRunPaciente());

        assertEquals("11111111-1", resultado.get().getRunMedico());
        assertEquals("paciente", resultado.get().getNombreMedico());
        assertEquals(28, resultado.get().getEdad());
        assertEquals("987654321", guardado.getNroTelefono());
        assertEquals("cirujano", guardado.getEspecialidad());
        assertEquals("firma-soto", guardado.getFirmaMedico());

    }

    @Test
    void debeListarMedicos() {
        repository.save(new Medico(          
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        ));

        repository.save(new Medico(  
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        ));

        List<Medico> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarMedico() {
        Medico medico = new Medico(            
            "22222222-2", 
            "Dra. Soto", 
            28,
            "987654321",
            "cirujano",
            "firma-soto"  
        );
        Medico guardado = repository.save(medico);

        repository.deleteById(guardado.getRunMedico());

        Optional<Medico> resultado = repository.findById(guardado.getRunMedico());
        assertFalse(resultado.isPresent());
    }
}