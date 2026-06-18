package com.example.ms_facturacion.y.presupuesto.service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_facturacion.y.presupuesto.client.MedicoClient;
import com.example.ms_facturacion.y.presupuesto.client.PacienteClient;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoDTO;
import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoResponse;
import com.example.ms_facturacion.y.presupuesto.model.FacturacionYPresupuesto;
import com.example.ms_facturacion.y.presupuesto.repository.FacturacionYPresupuestoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacturacionYPresupuestoService {

    private final FacturacionYPresupuestoRepository repository;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    public FacturacionYPresupuestoResponse  crear(FacturacionYPresupuestoDTO dto, String token) {

        log.info("Crear facturacion y presupuesto", keyValue("nombre del paciente", dto.getNombrePaciente()), keyValue("run del paciente", dto.getRunPaciente()), keyValue("nombre del medico", dto.getNombreMedico()), keyValue("run del medico", dto.getRunMedico()));

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("el paciente no existe no se puede crear la facturacion y el presupuesto");
        }

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);
        if (medico == null) {
                throw new RuntimeException("El médico no existe");
}

        FacturacionYPresupuesto facypre = repository.save(
                new FacturacionYPresupuesto(
                    null, 
                    dto.getPresupuesto(),
                    dto.getNombrePaciente(),    
                    dto.getRunPaciente(),
                    dto.getNombreMedico(),
                    dto.getRunMedico(),
                    dto.getTratamiento(),
                    dto.getDiasDuracion(),
                    dto.getGestionPagos()
                )
        );

        return mapToResponse(facypre, token);
    }

    public List<FacturacionYPresupuestoResponse> listar(String token) {

        return repository.findAll()
                .stream()
                .map(l -> mapToResponse(l, token))
                .toList();
    }

    public FacturacionYPresupuestoResponse obtener(Long id, String token) {

        FacturacionYPresupuesto facypre = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FacturacionYPresupuesto no encontrado"));

        return mapToResponse(facypre, token);
    }

    public FacturacionYPresupuestoResponse actualizar(Long id, FacturacionYPresupuestoDTO dto, String token) {

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("El paciente no existe");
        }

        FacturacionYPresupuesto facypre = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FacturacionYPresupuesto no encontrado"));

        facypre.setPresupuesto(dto.getPresupuesto());
        facypre.setNombrePaciente(dto.getNombrePaciente());
        facypre.setRunPaciente(dto.getRunPaciente());
        facypre.setNombreMedico(dto.getNombreMedico());
        facypre.setRunMedico(dto.getRunMedico());
        facypre.setTratamiento(dto.getTratamiento());
        facypre.setDiasDuracion(dto.getDiasDuracion());
        facypre.setGestionPagos(dto.getGestionPagos());

        return mapToResponse(repository.save(facypre), token);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private FacturacionYPresupuestoResponse mapToResponse(FacturacionYPresupuesto facypre, String token) {

        var paciente = pacienteClient.getPacienteClient(facypre.getRunPaciente(), token);
        var medico = medicoClient.getMedicoClient(facypre.getRunMedico(), token);

        return FacturacionYPresupuestoResponse.builder()
                .id(facypre.getId())
                .presupuesto(facypre.getPresupuesto())
                .paciente(paciente)
                .medico(medico)
                .tratamiento(facypre.getTratamiento())
                .diasDuracion(facypre.getDiasDuracion())
                .gestionPagos(facypre.getGestionPagos())
                .build();

    }
}
