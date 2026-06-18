package com.example.ms_registro.de.atenciones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_registro.de.atenciones.client.MedicoClient;
import com.example.ms_registro.de.atenciones.client.PacienteClient;
import com.example.ms_registro.de.atenciones.client.PagosClient;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesDTO;
import com.example.ms_registro.de.atenciones.dto.RegistroAtencionesResponse;
import com.example.ms_registro.de.atenciones.model.RegistroAtenciones;
import com.example.ms_registro.de.atenciones.repository.RegistroAtencionesRepository;


import static net.logstash.logback.argument.StructuredArguments.keyValue;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistroAtencionesService {
    
    private final RegistroAtencionesRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagosClient pagosClient;

    
    public RegistroAtencionesResponse  crear(RegistroAtencionesDTO dto, String token) {

        log.info("Crear registro de atenciones", keyValue("paciente run", dto.getRunpaciente()));

        
        var paciente = pacienteClient.getPacienteClient(dto.getRunpaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("el paciente no existe");
        }
        var medico = medicoClient.getMedicoClient(dto.getRunmedico(), token);
        if (medico == null) {
                throw new RuntimeException("El médico no existe");
}
            var pago = pagosClient.getPagosClient(dto.getIdPago(), token);
            
        if (pago == null) {
                throw new RuntimeException("El pago no existe");
        }

        RegistroAtenciones registro = repository.save(
                new RegistroAtenciones(
                    null,                 
                    dto.getNompaciente(), 
                    dto.getRunpaciente(), 
                    dto.getNommedico(), 
                    dto.getRunmedico(), 
                    dto.getTotal(), 
                    dto.getIdPago(),
                    dto.getFecha(), 
                    dto.getHora(), 
                    dto.getTratamientoRealizado()
                )
        );

        return mapToResponse(registro, token);
    }

    public List<RegistroAtencionesResponse> listar(String token) {

        return repository.findAll()
                .stream()
                .map(l -> mapToResponse(l, token))
                .toList();
    }

    public RegistroAtencionesResponse obtener(Long id, String token) {

        RegistroAtenciones registro = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de atenciones no se encontro"));

        return mapToResponse(registro, token);
    }

    public RegistroAtencionesResponse actualizar(Long id, RegistroAtencionesDTO dto, String token) {

         var paciente = pacienteClient.getPacienteClient(dto.getRunpaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("el paciente no existe");
        }
        var medico = medicoClient.getMedicoClient(dto.getRunmedico(), token);
        if (medico == null) {
                throw new RuntimeException("El médico no existe");
}
            var pago = pagosClient.getPagosClient(dto.getIdPago(), token);
            
        if (pago == null) {
                throw new RuntimeException("El pago no existe");
        }
        RegistroAtenciones r = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de atenciones no encontrado"));
                    r.setNompaciente(dto.getNompaciente());
                    r.setRunpaciente(dto.getRunpaciente());
                    r.setNommedico(dto.getNommedico());
                    r.setRunmedico(dto.getRunmedico());
                    r.setTotal(dto.getTotal());
                    r.setIdPago(dto.getIdPago());
                    r.setFecha(dto.getFecha());
                    r.setHora(dto.getHora());
                    r.setTratamientoRealizado(dto.getTratamientoRealizado());

        return mapToResponse(repository.save(r), token);
    
}
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private RegistroAtencionesResponse mapToResponse(RegistroAtenciones registro, String token) {

        var medico = medicoClient.getMedicoClient(registro.getRunmedico(), token);
        var paciente = pacienteClient.getPacienteClient(registro.getRunpaciente(), token);
        var pago = pagosClient.getPagosClient(registro.getIdPago(), token);

        return RegistroAtencionesResponse.builder()
                .id(registro.getId())
                .paciente(paciente)
                .medico(medico)
                .pago(pago)
                .fecha(registro.getFecha())
                .hora(registro.getHora())
                .tratamientoRealizado(registro.getTratamientoRealizado())
                .build();
    }
}
