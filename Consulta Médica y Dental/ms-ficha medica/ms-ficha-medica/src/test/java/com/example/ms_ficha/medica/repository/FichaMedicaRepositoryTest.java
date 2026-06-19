package test.java.com.example.ms_ficha.medica.repository;

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
        1L, "paciente","11111111-1",
         "medico","22222222-2","procedimiento",
    "queMedicamentoEstaTomando", "enfermedad",
    "alergias","odontograma");

        FichaMedica guardado = repository.save(fichaMedica);

        assertNotNull(guardado.getId());

    assertEquals("paciente", guardado.getNombrePaciente());
    assertEquals("11111111-1", guardado.getRunPaciente());
    assertEquals("alergias", guardado.getAlergias());
    assertEquals("enfermedad", guardado.getEnfermedad());
    assertEquals("queMedicamentoEstaTomando", guardado.getQueMedicamentoEstaTomando());

    assertEquals("medico", guardado.getNombreMedico());
    assertEquals("22222222-2", guardado.getRunMedico());

    assertEquals("procedimiento", guardado.getProcedimiento());
    assertEquals("odontograma", guardado.getOdontograma());
    }

    @Test
    void debeBuscarFichaMedicaPorId() {
    FichaMedica fichaMedica = new FichaMedica(        
         1L, "paciente","11111111-1",
         "22222222","medico","procedimiento",
    "queMedicamentoEstaTomando", "enfermedad",
    "alergias","odontograma");
        
        FichaMedica guardado = repository.save(fichaMedica);

        Optional<FichaMedica> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
    assertEquals("paciente", resultado.get().getNombrePaciente());
    assertEquals("11111111-1", resultado.get().getRunPaciente());
    assertEquals("alergias", resultado.get().getAlergias());
    assertEquals("enfermedad", resultado.get().getEnfermedad());
    assertEquals("queMedicamentoEstaTomando", resultado.get().getQueMedicamentoEstaTomando());

    assertEquals("medico", resultado.get().getNombreMedico());
    assertEquals("22222222-2", resultado.get().getRunMedico());

    assertEquals("procedimiento", resultado.getProcedimiento());
    assertEquals("odontograma", resultado.getOdontograma());

    }

    @Test
    void debeListarFichaMedica() {
        repository.save(new FichaMedica(        
            1L, "paciente","11111111-1",
         "medico","22222222-2","procedimiento",
    "queMedicamentoEstaTomando", "enfermedad",
    "alergias","odontograma"));
        
    repository.save(new FichaMedica(        
             1L, "paciente","11111111-1",
         "medico","22222222-2","procedimiento",
    "queMedicamentoEstaTomando", "enfermedad",
    "alergias","odontograma"));

        List<FichaMedica> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarFichaMedica() {
        FichaMedica fichaMedica = new FichaMedica(      
            1L, "paciente","11111111-1",
         "medico","22222222-2","procedimiento",
    "queMedicamentoEstaTomando", "enfermedad",
    "alergias","odontograma");

        FichaMedica guardado = repository.save(fichaMedica);

        repository.deleteById(guardado.getId());

        Optional<FichaMedica> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}