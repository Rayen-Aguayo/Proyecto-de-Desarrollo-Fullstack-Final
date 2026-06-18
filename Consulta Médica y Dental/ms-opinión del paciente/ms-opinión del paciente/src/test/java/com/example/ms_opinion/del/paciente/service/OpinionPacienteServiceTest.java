package com.example.ms_opinion.del.paciente.service;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_opinion.del.paciente.client.MedicoClient;
import com.example.ms_opinion.del.paciente.client.PacienteClient;
import com.example.ms_opinion.del.paciente.dto.MedicoResponse;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
import com.example.ms_opinion.del.paciente.dto.PacienteResponse;
import com.example.ms_opinion.del.paciente.model.OpinionPaciente;
import com.example.ms_opinion.del.paciente.repository.OpinionPacienteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpinionPacienteServiceTest {

    @Mock
    private OpinionPacienteRepository repo;

    @InjectMocks
    private OpinionPacienteService service;

    @Mock
    private MedicoClient medicoClient;
    
    @Mock
    private PacienteClient pacienteClient;

    @Test
    void deberiaRetornarOpinionPacienteExiste() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    OpinionPaciente opinionPaciente = new OpinionPaciente(
        1L,"1-1",
         "1-2","medico",7,
    "expliqueSuPuntuacion", "explicacionTratamiento",
    "comentarioMejora",7);

    when(repo.findById(1L)).thenReturn(Optional.of(opinionPaciente));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");

    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    OpinionPacienteResponse resultado = service.obtener(1L, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());

    assertNotNull(resultado.getPaciente());
    assertEquals("1-1", resultado.getPaciente().getRunPaciente());


    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals(7, resultado.getAtencionMedico());
    assertEquals("expliqueSuPuntuacion", resultado.getExpliqueSuPuntuacion());
    assertEquals("explicacionTratamiento", resultado.getExplicacionTratamiento());
    assertEquals("comentarioMejora", resultado.getComentarioMejora());
    assertEquals(7, resultado.getPuntuacionMedico());

    verify(repo).findById(1L);
}
@Test
void deberiaLanzarExcepcionCuandoOpinionPacienteNoExiste() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.obtener(99L, tokenDePrueba)
    );

    assertEquals("Opinión no encontrada", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaRetornarOpinionPaciente() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    OpinionPaciente opinionPaciente = new OpinionPaciente( 
        1L,"1-1",
         "1-2","medico",7,
    "expliqueSuPuntuacion", "explicacionTratamiento",
    "comentarioMejora",7);

    when(repo.findAll()).thenReturn(List.of(opinionPaciente));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
 
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    List<OpinionPacienteResponse> resultado = service.listar(null);

    // Assert
    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());

    OpinionPacienteResponse item = resultado.get(0);

    assertNotNull(item.getPaciente());
    assertEquals("1-1", item.getPaciente().getRunPaciente());   


    assertNotNull(item.getMedico());
    assertEquals("medico", item.getMedico().getNombreMedico());
    assertEquals("1-2", item.getMedico().getRunMedico());

    assertEquals(7, item.getAtencionMedico());
    assertEquals("expliqueSuPuntuacion", item.getExpliqueSuPuntuacion());
    assertEquals("explicacionTratamiento", item.getExplicacionTratamiento());
    assertEquals("comentarioMejora", item.getComentarioMejora());
    assertEquals(7, item.getPuntuacionMedico());
 
    verify(repo).findAll();
}
@Test
void deberiaRetornarListaVaciaDeOpinionPaciente() {
    // Arrange
    when(repo.findAll()).thenReturn(List.of());
    // Act
    List<OpinionPacienteResponse> resultado = service.listar(null);

    // Assert
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(repo).findAll();
}

@Test
void deberiaCrearOpinionPacienteCorrectamente() {
    
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    OpinionPacienteDTO dto = new OpinionPacienteDTO();
                    
                        dto.setRunPaciente("1-1");
                        dto.setRunMedico("1-2");
                        dto.setNombreMedico("medico");
                        dto.setAtencionMedico(7);
                        dto.setExpliqueSuPuntuacion("expliqueSuPuntuacion");
                        dto.setExplicacionTratamiento("explicacionTratamiento");
                        dto.setComentarioMejora("comentarioMejora");
                        dto.setPuntuacionMedico(7);


    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    OpinionPaciente guardado = new OpinionPaciente( 
         1L,"1-1",
         "1-2","medico",7,
    "expliqueSuPuntuacion", "explicacionTratamiento",
    "comentarioMejora",7);
    when(repo.save(any(OpinionPaciente.class))).thenReturn(guardado);

    // Act
    OpinionPacienteResponse resultado = service.crear(dto,tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertNotNull(resultado.getPaciente());
    assertEquals("1-1", resultado.getPaciente().getRunPaciente());


    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals(7, resultado.getAtencionMedico());
    assertEquals("expliqueSuPuntuacion", resultado.getExpliqueSuPuntuacion());
    assertEquals("explicacionTratamiento", resultado.getExplicacionTratamiento());
    assertEquals("comentarioMejora", resultado.getComentarioMejora());
    assertEquals(7, resultado.getPuntuacionMedico());

    verify(repo).save(any(OpinionPaciente.class));
}
@Test
void deberiaLanzarExcepcionCuandoPacienteNoExisteAlCrear() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    OpinionPacienteDTO dto = new OpinionPacienteDTO();
    dto.setRunPaciente("1-1");
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(null); 


    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("El paciente no existe, no se puede registrar la opinión", ex.getMessage());
    verify(repo, never()).save(any()); 
}

@Test
void deberiaLanzarExcepcionCuandoMedicoNoExisteAlCrear() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";
    OpinionPacienteDTO dto = new OpinionPacienteDTO();
    dto.setRunPaciente("1-1");
    dto.setRunMedico("1-2");

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(null);

    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("El médico no existe, no se puede registrar la opinión", ex.getMessage()); 
    verify(repo, never()).save(any());
}

@Test
void deberiaEliminarOpinionPacientePorId() {
    // Arrange
    
    doNothing().when(repo).deleteById(1L);

    // Act
    service.eliminar(1L);

    // Assert
    verify(repo).deleteById(1L);
}

@Test
void deberiaLanzarExcepcionCuandoOpinionPacienteNoSeEliminoCorectamente() {
    // Arrange
    when(repo.existsById(99L)).thenReturn(false); 

    // Act + Assert
    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.eliminar(99L)
    );

    assertEquals("No se puede eliminar, opinión no encontrada", ex.getMessage());
    verify(repo).findById(99L);
    verify(repo, never()).deleteById(99L); 
}
}