package com.example.ms_ficha.medica.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_ficha.medica.client.MedicoClient;
import com.example.ms_ficha.medica.client.PacienteClient;
import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.dto.MedicoResponse;
import com.example.ms_ficha.medica.dto.PacienteResponse;
import com.example.ms_ficha.medica.model.FichaMedica;
import com.example.ms_ficha.medica.repository.FichaMedicaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class FichaMedicaServiceTest {

    @Mock
    private FichaMedicaRepository repo;

    @InjectMocks
    private FichaMedicaService service;

    @Mock
    private MedicoClient medicoClient;
    
    @Mock
    private PacienteClient pacienteClient;

    @Test
    void deberiaRetornarFichaMedicaExiste() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica fichaMedica = new FichaMedica(
        1L, 
        "11111111-1",
        "paciente",
        "22222222-2",
        "medico",
        "procedimiento",
        "queMedicamentoEstaTomando", 
        "enfermedad",
        "alergias",
        "odontograma");

    when(repo.findById(1L)).thenReturn(Optional.of(fichaMedica));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    pacienteResponse.setAlergias("alergias");
    pacienteResponse.setEnfermedad("enfermedad");
    pacienteResponse.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");
    
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    FichaMedicaResponse resultado = service.obtener(1L, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());

    assertNotNull(resultado.getPaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("alergias", resultado.getPaciente().getAlergias());
    assertEquals("enfermedad", resultado.getPaciente().getEnfermedad());
    assertEquals("queMedicamentoEstaTomando", resultado.getQueMedicamentoEstaTomando());


    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("22222222-2", resultado.getMedico().getRunMedico());

    assertEquals("procedimiento", resultado.getProcedimiento());
    assertEquals("odontograma", resultado.getOdontograma());

    verify(repo).findById(1L);
}
@Test
void deberiaLanzarExcepcionCuandoFichaMedicaNoExiste() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.obtener(99L, tokenDePrueba)
    );

    assertEquals("Ficha médica no encontrada", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaRetornarListaFichaMedica() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica fichaMedica = new FichaMedica( 
        1L, 
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

    when(repo.findAll()).thenReturn(List.of(fichaMedica));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    pacienteResponse.setAlergias("alergias");
    pacienteResponse.setEnfermedad("enfermedad");
    pacienteResponse.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    List<FichaMedicaResponse> resultado = service.listar(null);

    // Assert
    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());

    FichaMedicaResponse item = resultado.get(0);

    assertNotNull(item.getPaciente());
    assertEquals("11111111-1", item.getPaciente().getRunPaciente());   
    assertEquals("paciente", item.getPaciente().getNombrePaciente());
    assertEquals("alergias", item.getPaciente().getAlergias());
    assertEquals("enfermedad", item.getPaciente().getEnfermedad());
    assertEquals("QueMedicamentoEstaTomando", item.getPaciente().getQueMedicamentoEstaTomando());

    assertNotNull(item.getMedico());
    assertEquals("medico", item.getMedico().getNombreMedico());
    assertEquals("22222222-2", item.getMedico().getRunMedico());

    assertEquals("procedimiento", item.getProcedimiento());
    assertEquals("odontograma", item.getOdontograma());

    verify(repo).findAll();
}
@Test
void deberiaRetornarListaVaciaDeFichaMedica() {
    // Arrange
    when(repo.findAll()).thenReturn(List.of());
    // Act
    List<FichaMedicaResponse> resultado = service.listar(null);

    // Assert
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(repo).findAll();
}

@Test
void deberiaCrearFichaMedicaCorrectamente() {
    
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedicaDTO dto = new FichaMedicaDTO();
                    
                    dto.setRunPaciente("11111111-1");
                    dto.setNombrePaciente("paciente");
                    dto.setRunMedico("22222222-2");
                    dto.setNombreMedico("medico");
                    dto.setProcedimiento("Procedimiento");
                    dto.setQueMedicamentoEstaTomando("QueMedicamentoEstaTomando");
                    dto.setEnfermedad("Enfermedad");
                    dto.setAlergias("Alergias");
                    dto.setOdontograma("Odontograma");


    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    pacienteResponse.setAlergias("alergias");
    pacienteResponse.setEnfermedad("enfermedad");
    pacienteResponse.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");
    
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    FichaMedica guardado = new FichaMedica( 
        1L, 
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
    when(repo.save(any(FichaMedica.class))).thenReturn(guardado);

    // Act
    FichaMedicaResponse resultado = service.crear(dto,tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertNotNull(resultado.getPaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("alergias", resultado.getPaciente().getAlergias());
    assertEquals("enfermedad", resultado.getPaciente().getEnfermedad());
    assertEquals("queMedicamentoEstaTomando", resultado.getQueMedicamentoEstaTomando());


    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("22222222-2", resultado.getMedico().getRunMedico());

    assertEquals("procedimiento", resultado.getProcedimiento());
    assertEquals("odontograma", resultado.getOdontograma());

    verify(repo).save(any(FichaMedica.class));
}
@Test
void deberiaLanzarExcepcionCuandoPacienteNoExisteAlCrear() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedicaDTO dto = new FichaMedicaDTO();
    dto.setRunPaciente("11111111-1");
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(null); 


    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("El paciente no existe no se puede crear la Ficha medica", ex.getMessage());
    verify(repo, never()).save(any()); 
}

@Test
void deberiaLanzarExcepcionCuandoMedicoNoExisteAlCrear() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";
    FichaMedicaDTO dto = new FichaMedicaDTO();
    dto.setRunPaciente("11111111-1");
    dto.setRunMedico("22222222-2");

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(null);

    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("El médico no existe no se puede crear la Ficha medica", ex.getMessage()); 
    verify(repo, never()).save(any());
}

@Test
void deberiaActualizarFichaMedicaCorrectamente() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica existente = new FichaMedica( 
        1L, 
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
    
    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    pacienteResponse.setAlergias("alergias");
    pacienteResponse.setEnfermedad("enfermedad");
    pacienteResponse.setQueMedicamentoEstaTomando("queMedicamentoEstaTomando");
    
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);


    FichaMedicaDTO dto = new FichaMedicaDTO();
                    dto.setRunPaciente("11111111-1");
                    dto.setNombrePaciente("paciente");
                    dto.setRunMedico("22222222-2");
                    dto.setNombreMedico("medico");
                    dto.setProcedimiento("Procedimiento");
                    dto.setQueMedicamentoEstaTomando("QueMedicamentoEstaTomando");
                    dto.setEnfermedad("Enfermedad");
                    dto.setAlergias("Alergias");
                    dto.setOdontograma("Odontograma");


    when(repo.findById(1L)).thenReturn(Optional.of(existente));
    when(repo.save(any(FichaMedica.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    FichaMedicaResponse resultado = service.actualizar(1L, dto, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertNotNull(resultado.getPaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("alergias", resultado.getPaciente().getAlergias());
    assertEquals("enfermedad", resultado.getPaciente().getEnfermedad());
    assertEquals("queMedicamentoEstaTomando", resultado.getQueMedicamentoEstaTomando());


    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("22222222-2", resultado.getMedico().getRunMedico());

    assertEquals("Procedimiento", resultado.getProcedimiento());
    assertEquals("Odontograma", resultado.getOdontograma());

    verify(repo).findById(1L);
    verify(repo).save(existente);
}
@Test
void deberiaLanzarExcepcionCuandoFichaMedicaNoSeActualizoCorectamente() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,  
            () -> service.actualizar(99L, null, tokenDePrueba)
    );

    assertEquals("Ficha médica no encontrada", ex.getMessage()); 
    verify(repo).findById(99L);
}

@Test
void deberiaEliminarFichaMedicaPorId() {
    // Arrange
    
    doNothing().when(repo).deleteById(1L);

    // Act
    service.eliminar(1L);

    // Assert
    verify(repo).deleteById(1L);
}

@Test
void deberiaLanzarExcepcionCuandoFichaMedicaNoSeEliminoCorectamente() {
    // Arrange
    when(repo.existsById(99L)).thenReturn(false); 

    // Act + Assert
    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.eliminar(99L)
    );

    assertEquals("Ficha médica no encontrada", ex.getMessage());
    verify(repo).findById(99L);
    verify(repo, never()).deleteById(99L); 
}

}
