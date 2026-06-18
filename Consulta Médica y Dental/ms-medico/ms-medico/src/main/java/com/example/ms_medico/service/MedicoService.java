package com.example.ms_medico.service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.repository.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public Medico crear(MedicoDTO dto) {
        log.info("Crear medico", keyValue("nombre", dto.getNombreMedico()));


        Medico m = new Medico(dto.getRunMedico(),dto.getNombreMedico(),dto.getEdad(), 
        dto.getNroTelefono(), dto.getEspecialidad(), dto.getFirmaMedico());

        return medicoRepository.save(m);
    }

    public List<Medico> listar() {
        log.info("Listar Medicos");
        return medicoRepository.findAll();
    }

    public Medico obtener(String id) {              
        log.info("Obtener Medico", keyValue("run", id));

        return medicoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Medico no encontrado"));
    }

    public Medico actualizar(String id, MedicoDTO dto) { 
        log.info("Actualizar Medico", keyValue("id", id));

        Medico m = obtener(id);  
        m.setNombreMedico(dto.getNombreMedico());
        m.setEspecialidad(dto.getEspecialidad());
        m.setEdad(dto.getEdad());
        m.setNroTelefono(dto.getNroTelefono());
        m.setFirmaMedico(dto.getFirmaMedico());

        return medicoRepository.save(m);
    }

    public void eliminar(String id) {
        log.warn("Eliminar Medico", keyValue("run", id));
        medicoRepository.deleteById(id);
    }
}
