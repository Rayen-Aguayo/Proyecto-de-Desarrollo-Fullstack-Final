package com.example.ms_ficha.medica.service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_ficha.medica.client.MedicoClient;
import com.example.ms_ficha.medica.client.PacienteClient;
import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.model.FichaMedica;
import com.example.ms_ficha.medica.repository.FichaMedicaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FichaMedicaService {

    private final FichaMedicaRepository fichaMedicaRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public FichaMedicaResponse crear(FichaMedicaDTO dto, String token) {

        log.info("Creando ficha médica",
                keyValue("paciente", dto.getRunPaciente()));

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);
        if (paciente == null) {
            log.warn("Paciente no encontrado al crear ficha médica",
                    keyValue("paciente", dto.getRunPaciente()));
            throw new EntityNotFoundException("El paciente no existe, no se puede crear la Ficha médica");
        }

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);
        if (medico == null) {
            log.warn("Médico no encontrado al crear ficha médica",
                    keyValue("medico", dto.getRunMedico()));
            throw new EntityNotFoundException("El médico no existe, no se puede crear la Ficha médica");
        }

        FichaMedica fichaMedica = fichaMedicaRepository.save(
                new FichaMedica(
                        null,
                        dto.getRunPaciente(),
                        dto.getNombrePaciente(),
                        dto.getRunMedico(),
                        dto.getNombreMedico(),
                        dto.getProcedimiento(),
                        dto.getQueMedicamentoEstaTomando(),
                        dto.getEnfermedad(),
                        dto.getAlergias(),
                        dto.getOdontograma()));

        log.info("Ficha médica creada",
                keyValue("id", fichaMedica.getId()));

        return mapToResponse(fichaMedica, token);
    }

    public List<FichaMedicaResponse> listar(String token) {
        return fichaMedicaRepository.findAll()
                .stream()
                .map(ficha -> mapToResponse(ficha, token))
                .toList();
    }

    public FichaMedicaResponse obtener(Long id, String token) {
        FichaMedica fichaMedica = fichaMedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha médica no encontrada"));
        return mapToResponse(fichaMedica, token);
    }

    public FichaMedicaResponse actualizar(Long id, FichaMedicaDTO dto, String token) {

        FichaMedica ficha = fichaMedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha médica no encontrada"));

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);
        if (paciente == null) {
            log.warn("Paciente no encontrado al actualizar ficha médica",
                    keyValue("paciente", dto.getRunPaciente()));
            throw new EntityNotFoundException("El paciente no existe");
        }

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);
        if (medico == null) {
            log.warn("Médico no encontrado al actualizar ficha médica",
                    keyValue("medico", dto.getRunMedico()));
            throw new EntityNotFoundException("El médico no existe");
        }

        ficha.setRunPaciente(dto.getRunPaciente());
        ficha.setNombrePaciente(dto.getNombrePaciente());
        ficha.setRunMedico(dto.getRunMedico());
        ficha.setNombreMedico(dto.getNombreMedico());
        ficha.setProcedimiento(dto.getProcedimiento());
        ficha.setQueMedicamentoEstaTomando(dto.getQueMedicamentoEstaTomando());
        ficha.setEnfermedad(dto.getEnfermedad());
        ficha.setAlergias(dto.getAlergias());
        ficha.setOdontograma(dto.getOdontograma());

        FichaMedica actualizada = fichaMedicaRepository.save(ficha);

        log.info("Ficha médica actualizada",
                keyValue("id", actualizada.getId()));

        return mapToResponse(actualizada, token);
    }

    public void eliminar(Long id) {
        FichaMedica fichaMedica = fichaMedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha médica no encontrada"));
        fichaMedicaRepository.delete(fichaMedica);
        log.info("Ficha médica eliminada",
                keyValue("id", id));
    }

    private FichaMedicaResponse mapToResponse(FichaMedica fichaMedica, String token) {

        var paciente = pacienteClient.getPacienteClient(fichaMedica.getRunPaciente(), token);
        if (paciente == null) {
            log.warn("Paciente asociado no encontrado al mapear ficha médica",
                    keyValue("id", fichaMedica.getId()),
                    keyValue("paciente", fichaMedica.getRunPaciente()));
            throw new EntityNotFoundException("Paciente asociado a la ficha médica no encontrado");
        }

        var medico = medicoClient.getMedicoClient(fichaMedica.getRunMedico(), token);
        if (medico == null) {
            log.warn("Médico asociado no encontrado al mapear ficha médica",
                    keyValue("id", fichaMedica.getId()),
                    keyValue("medico", fichaMedica.getRunMedico()));
            throw new EntityNotFoundException("Médico asociado a la ficha médica no encontrado");
        }

        return FichaMedicaResponse.builder()
                .id(fichaMedica.getId())
                .paciente(paciente)
                .medico(medico)
                .procedimiento(fichaMedica.getProcedimiento())
                .queMedicamentoEstaTomando(fichaMedica.getQueMedicamentoEstaTomando())
                .enfermedad(fichaMedica.getEnfermedad())
                .alergias(fichaMedica.getAlergias())
                .odontograma(fichaMedica.getOdontograma())
                .build();
    }
}