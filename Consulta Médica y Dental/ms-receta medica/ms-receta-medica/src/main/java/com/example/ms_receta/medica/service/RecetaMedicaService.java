package com.example.ms_receta.medica.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_receta.medica.client.MedicoClient;
import com.example.ms_receta.medica.dto.RecetaMedicaDTO;
import com.example.ms_receta.medica.dto.RecetaMedicaResponce;
import com.example.ms_receta.medica.model.RecetaMedica;
import com.example.ms_receta.medica.repository.RecetaMedicaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecetaMedicaService {

    private final RecetaMedicaRepository recetamedicaRepository;
    private final MedicoClient medicoClient;

    public RecetaMedicaResponce  crear(RecetaMedicaDTO dto, String token) {

        log.info("Crear Receta medica", keyValue("run medico", dto.getRunMedico()));

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);

        if (medico == null) {
            throw new RuntimeException("Médico no existe");
        }

        RecetaMedica recetamedica = recetamedicaRepository.save(
                new RecetaMedica(
                    null,
                    dto.getNomMedicamento(),
                    dto.getDiasTomarMedicamento(),
                    dto.getInicioReceta(),
                    dto.getNomMedico(),
                    dto.getRunMedico(),
                    dto.getCantTomarDia(),
                    dto.getFirmaMedico())      
        );

        return mapToResponse(recetamedica, token);
    }

    public List<RecetaMedicaResponce> listar(String token) {

        return recetamedicaRepository.findAll()
                .stream()
                .map(l -> mapToResponse(l, token))
                .toList();
    }

    public RecetaMedicaResponce obtener(Long id, String token) {

        RecetaMedica recetamedica = recetamedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta médica no encontrada"));

        return mapToResponse(recetamedica, token);
    }

    public RecetaMedicaResponce actualizar(Long id, RecetaMedicaDTO dto, String token) {

        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);

        if (medico == null) {
            throw new RuntimeException("Médico no existe");
        }

        RecetaMedica r = recetamedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta médica no encontrada"));

        r.setNomMedicamento(dto.getNomMedicamento());
        r.setDiasTomarMedicamento(dto.getDiasTomarMedicamento());
        r.setInicioReceta(dto.getInicioReceta());
        r.setNomMedico(dto.getNomMedico());
        r.setRunMedico(dto.getRunMedico());
        r.setCantTomarDia(dto.getCantTomarDia());
        r.setFirmaMedico(dto.getFirmaMedico());

        return mapToResponse(recetamedicaRepository.save(r), token);
    }

    public void eliminar(Long id) {
        recetamedicaRepository.deleteById(id);
    }

    private RecetaMedicaResponce mapToResponse(RecetaMedica recetaMedica, String token) {

        var medico = medicoClient.getMedicoClient(recetaMedica.getRunMedico(), token);

        return RecetaMedicaResponce.builder()
                .id(recetaMedica.getId())
                .nomMedicamento(recetaMedica.getNomMedicamento())
                .diasTomarMedicamento(recetaMedica.getDiasTomarMedicamento())
                .inicioReceta(recetaMedica.getInicioReceta())
                .medico(medico)
                .cantTomarDia(recetaMedica.getCantTomarDia())
                .build();

    }
}
