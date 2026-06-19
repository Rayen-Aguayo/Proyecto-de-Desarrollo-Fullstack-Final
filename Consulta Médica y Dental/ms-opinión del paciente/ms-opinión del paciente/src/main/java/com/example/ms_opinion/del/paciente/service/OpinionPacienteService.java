package com.example.ms_opinion.del.paciente.service;

import org.springframework.stereotype.Service;

import com.example.ms_opinion.del.paciente.client.MedicoClient;
import com.example.ms_opinion.del.paciente.client.PacienteClient;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
import com.example.ms_opinion.del.paciente.model.OpinionPaciente;
import com.example.ms_opinion.del.paciente.repository.OpinionPacienteRepository;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpinionPacienteService {

    private final OpinionPacienteRepository opinionPacienteRepository;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    public OpinionPacienteResponse crear(OpinionPacienteDTO dto, String token) {

        log.info("Registrando nueva opinión para el paciente: {}", keyValue("run del paciente", dto.getRunPaciente()));
        
        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);
        if (paciente == null) {
            throw new RuntimeException("El paciente no existe, no se puede registrar la opinión");
        }

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(),token);

        if (medico == null) {
            throw new RuntimeException("El médico no existe, no se puede registrar la opinión");
        }

        OpinionPaciente opinionPaciente = opinionPacienteRepository.save(
                new OpinionPaciente(
                        null,
                        dto.getRunPaciente(),
                        dto.getRunMedico(),
                        dto.getNombreMedico(),
                        dto.getAtencionMedico(),
                        dto.getExpliqueSuPuntuacion(),
                        dto.getExplicacionTratamiento(),
                        dto.getComentarioMejora(),
                        dto.getPuntuacionMedico())
        );

        return mapToResponse(opinionPaciente, token);

    }

    public List<OpinionPacienteResponse> listar(String token) {
        return opinionPacienteRepository.findAll()
                .stream()
                .map(o -> mapToResponse(o, token))
                .toList();
    }

    public OpinionPacienteResponse obtener(Long id,String token) {
        OpinionPaciente opinion = opinionPacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opinión no encontrada"));
        return mapToResponse(opinion, token);
    }

    public void eliminar(Long id) {
        if (!opinionPacienteRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar, opinión no encontrada");
        }
        opinionPacienteRepository.deleteById(id);
    }

    
    private OpinionPacienteResponse mapToResponse(OpinionPaciente opinion, String token) {
        var paciente = pacienteClient.getPacienteClient(opinion.getRunPaciente(), token);
        var medico = medicoClient.getMedicoClient(opinion.getRunMedico(), token);
        return OpinionPacienteResponse.builder()
                .id(opinion.getId())
                .paciente(paciente)
                .medico(medico)
                .atencionMedico(opinion.getAtencionMedico())
                .expliqueSuPuntuacion(opinion.getExpliqueSuPuntuacion())
                .explicacionTratamiento(opinion.getExplicacionTratamiento())
                .comentarioMejora(opinion.getComentarioMejora())
                .puntuacionMedico(opinion.getPuntuacionMedico())
                .build();
    }

    public Object actualizar(long eq, OpinionPacienteDTO any, String anyString) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }
}
