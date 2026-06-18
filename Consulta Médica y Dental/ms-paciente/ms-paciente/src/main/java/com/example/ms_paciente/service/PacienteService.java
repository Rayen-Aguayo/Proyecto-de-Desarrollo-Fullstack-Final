package com.example.ms_paciente.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.ms_paciente.dto.PacienteDTO;
import com.example.ms_paciente.model.Paciente;
import com.example.ms_paciente.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public Paciente crear(PacienteDTO dto) {
        log.info("Crear paciente", keyValue("nombre", dto.getNombrePaciente()));

        Paciente p = new Paciente(dto.getRunPaciente(), dto.getNombrePaciente(), 
        dto.getDatosDelPaciente(), dto.getEdadPaciente(), dto.getAlergias(), 
        dto.getEnfermedad(), dto.getQueMedicamentoEstaTomando(), dto.getNroTelefono());
 
        return pacienteRepository.save(p);
    }

    public List<Paciente> listar() {
        log.info("Listar pacientes");
        return pacienteRepository.findAll();
    }

    public Paciente obtener(String id) {
        log.info("Obtener paciente", keyValue("run", id));

        return pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("paciente no encontrado"));
    }

    public Paciente actualizar(String id, PacienteDTO dto) {
        log.info("Actualizar paciente", keyValue("run", id));

        Paciente p = obtener(id);
        p.setNombrePaciente(dto.getNombrePaciente()); 
        p.setDatosDelPaciente(dto.getDatosDelPaciente());
        p.setEdadPaciente(dto.getEdadPaciente());
        p.setAlergias(dto.getAlergias()); 
        p.setEnfermedad(dto.getEnfermedad());
        p.setQueMedicamentoEstaTomando(dto.getQueMedicamentoEstaTomando());        
        p.setNroTelefono(dto.getNroTelefono());



        return pacienteRepository.save(p);
    }

    public void eliminar(String id) {
        log.warn("Eliminar paciente", keyValue("run", id));
        pacienteRepository.deleteById(id);
    }
}
