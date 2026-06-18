package com.example.ms_facturacion.y.presupuesto.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_facturacion.y.presupuesto.client.MedicoClient;
import com.example.ms_facturacion.y.presupuesto.client.PacienteClient;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoDTO;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoResponse;
import com.example.ms_facturacion.y.presupuesto.dto.MedicoResponse;
import com.example.ms_facturacion.y.presupuesto.dto.PacienteResponse;
import com.example.ms_facturacion.y.presupuesto.model.FacturacionYPresupuesto;
import com.example.ms_facturacion.y.presupuesto.repository.FacturacionYPresupuestoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturacionYPresupuestoServiceTest {

    @Mock
    private FacturacionYPresupuestoRepository repo;

    @InjectMocks
    private FacturacionYPresupuestoService service;

    @Mock
    private MedicoClient medicoClient;
    
    @Mock
    private PacienteClient pacienteClient;

    @Test
    void deberiaRetornarFacturacionYPresupuestoExiste() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FacturacionYPresupuesto facYpre = new FacturacionYPresupuesto(
        1L, 30.000, "paciente","11111111-1",
         "medico","22222222-2","tratamiento",
    8, "gestionPagos");
    when(repo.findById(1L)).thenReturn(Optional.of(facYpre));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    FacturacionYPresupuestoResponse resultado = service.obtener(1L, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("22222222-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).findById(1L);
}
@Test
void deberiaLanzarExcepcionCuandoFacturacionYPresupuestoNoExiste() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.obtener(99L, tokenDePrueba)
    );

    assertEquals("FacturacionYPresupuesto no encontrado", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaRetornarListaFacturacionYPresupuesto() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FacturacionYPresupuesto facYpre = new FacturacionYPresupuesto( 1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos");

    when(repo.findAll()).thenReturn(List.of(facYpre));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    List<FacturacionYPresupuestoResponse> resultado = service.listar(null);

    // Assert
    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());

    FacturacionYPresupuestoResponse item = resultado.get(0);

    assertEquals(30.000, item.getPresupuesto());

    assertNotNull(item.getPaciente());
    assertEquals("paciente", item.getPaciente().getNombrePaciente());
    assertEquals("11111111-1", item.getPaciente().getRunPaciente());

    assertNotNull(item.getMedico());
    assertEquals("medico", item.getMedico().getNombreMedico());
    assertEquals("22222222-2", item.getMedico().getRunMedico());

    assertEquals("tratamiento", item.getTratamiento());
    assertEquals(8, item.getDiasDuracion());
    assertEquals("gestionPagos", item.getGestionPagos());

    verify(repo).findAll();
}
@Test
void deberiaRetornarListaVaciaDeFacturacionYPresupuesto() {
    // Arrange
    when(repo.findAll()).thenReturn(List.of());
    // Act
    List<FacturacionYPresupuestoResponse> resultado = service.listar(null);

    // Assert
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(repo).findAll();
}

@Test
void deberiaCrearFacturacionYPresupuestoCorrectamente() {
    
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
                    
                    dto.setPresupuesto(30.000);
                    dto.setNombrePaciente("paciente");    
                    dto.setRunPaciente("11111111-1");
                    dto.setNombreMedico("medico");
                    dto.setRunMedico("22222222-2");
                    dto.setTratamiento("tratamiento");
                    dto.setDiasDuracion(8);
                    dto.setGestionPagos("gestionPagos");


    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);

    FacturacionYPresupuesto Guardado = new FacturacionYPresupuesto(1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos");
    when(repo.save(any(FacturacionYPresupuesto.class))).thenReturn(Guardado);

    // Act
    FacturacionYPresupuestoResponse resultado = service.crear(dto,tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).save(any(FacturacionYPresupuesto.class));
}
@Test
void deberiaLanzarExcepcionCuandoPacienteNoExisteAlCrear() {
    // Arrange
    FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
    String tokenDePrueba = "Bearer token-prueba";

    dto.setRunPaciente("11111111-1");
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(null); 

    dto.setRunMedico("22222222-2");
    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(null); 

    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("el paciente no existe no se puede crear la facturacion y el presupuesto", ex.getMessage());
    verify(repo, never()).save(any()); 
}

@Test
void deberiaLanzarExcepcionCuandoMedicoNoExisteAlCrear() {
    // Arrange
    FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
    String tokenDePrueba = "Bearer token-prueba";
    
    dto.setRunPaciente("11111111-1");
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(null);

    dto.setRunMedico("22222222-2");
    
    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(null); 
    // Act + Assert
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crear(dto, tokenDePrueba)
    );

    assertEquals("El médico no existe no se puede crear la facturacion y el presupuesto", ex.getMessage());
    verify(repo, never()).save(any());
}

@Test
void deberiaActualizarFacturacionYPresupuestoCorrectamente() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FacturacionYPresupuesto existente = new FacturacionYPresupuesto(1L, 30.000, "paciente","1-1",
     "medico","22222222-2","tratamiento",
    8, "gestionPagos");
    
    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("11111111-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("22222222-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medicoResponse);


    FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
                    dto.setPresupuesto(30.000);
                    dto.setNombrePaciente("paciente");    
                    dto.setRunPaciente("11111111-1");
                    dto.setNombreMedico("medico");
                    dto.setRunMedico("22222222-2");
                    dto.setTratamiento("tratamiento");
                    dto.setDiasDuracion(8);
                    dto.setGestionPagos("gestionPagos");


    when(repo.findById(1L)).thenReturn(Optional.of(existente));
    when(repo.save(any(FacturacionYPresupuesto.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    FacturacionYPresupuestoResponse resultado = service.actualizar(1L, dto, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("11111111-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("22222222-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).findById(1L);
    verify(repo).save(existente);
}
@Test
void deberiaLanzarExcepcionCuandoFacturacionYPresupuestoNoSeActualizoCorectamente() {
    // Arrange
    FacturacionYPresupuestoDTO dto = new FacturacionYPresupuestoDTO();
    String tokenDePrueba = "Bearer token-prueba";
    dto.setRunPaciente("11111111-1");
    dto.setRunMedico("22222222-2");

    PacienteResponse paciente = new PacienteResponse();
    paciente.setRunPaciente("11111111-1");
    paciente.setNombrePaciente("Juan Pérez");

    MedicoResponse medico = new MedicoResponse();
    medico.setRunMedico("22222222-2");
    medico.setNombreMedico("Dra. Soto");

    when(pacienteClient.getPacienteClient("11111111-1", tokenDePrueba)).thenReturn(paciente);
    when(medicoClient.getMedicoClient("22222222-2", tokenDePrueba)).thenReturn(medico);
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.actualizar(99L, dto, tokenDePrueba)
    );

    assertEquals("FacturacionYPresupuesto no encontrado", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaEliminarFacturacionYPresupuestoPorId() {
    // Arrange
    
    doNothing().when(repo).deleteById(1L);

    // Act
    service.eliminar(1L);

    // Assert
    verify(repo).deleteById(1L);
}

@Test
void deberiaLanzarExcepcionCuandoFacturacionYPresupuestoNoSeEliminoCorectamente() {
    // Arrange
    when(repo.existsById(99L)).thenReturn(false); 

    // Act + Assert
    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.eliminar(99L)
    );

    assertEquals("Facturacion y presupuesto no encontrado", ex.getMessage());
    verify(repo).existsById(99L);
    verify(repo, never()).deleteById(99L); 
}

}
