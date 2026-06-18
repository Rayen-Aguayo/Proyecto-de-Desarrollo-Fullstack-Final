package com.example.ms_reserva.y.anular.hora.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_reserva.y.anular.hora.client.MedicoClient;
import com.example.ms_reserva.y.anular.hora.client.PacienteClient;
import com.example.ms_reserva.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reserva.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reserva.y.anular.hora.model.PedirHora;
import com.example.ms_reserva.y.anular.hora.repository.PedirHoraRepository;

import static net.logstash.logback.argument.StructuredArguments.keyValue;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedirHoraService {

    private final PedirHoraRepository pedirHoraRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;


    public PedirHoraResponse  crear(PedirHoraDTO dto, String token) {

        log.info("reservar hora", keyValue("run del paciente", dto.getRunPaciente()));
        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);
        if (paciente == null) {
            throw new RuntimeException("el paciente no existe no se le puede reservar una hora");
        }
        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);
        if (medico == null) {
                throw new RuntimeException("El médico no existe");
        }

        PedirHora pedirHora = pedirHoraRepository.save(
               new PedirHora(
                        null,
                        dto.getRunPaciente(),
                        dto.getNombrePaciente(),
                        dto.getRunMedico(),
                        dto.getNombreMedico(),
                        dto.getFecha(),
                        dto.getHoraDeAtencion(),
                        dto.getAtencion()
                    )
        );

        return mapToResponse(pedirHora, token);
    }

    public List<PedirHoraResponse> listar(String token) {

        return pedirHoraRepository.findAll()
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    public PedirHoraResponse obtener(Long id, String token) {

        PedirHora pedirHora = pedirHoraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no se a encontrado ninguna reserva de hora"));

        return mapToResponse(pedirHora, token);
    }

    public PedirHoraResponse actualizar(Long id, PedirHoraDTO dto, String token) {

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("el paciente no existe");
        }
        var medico = medicoClient.getMedicoClient(dto.getRunMedico(), token);

        if (medico == null) {
        throw new RuntimeException("El médico no existe");
        }
        PedirHora p = pedirHoraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reserva de hora no encontrado"));


        p.setRunPaciente(dto.getRunPaciente());
        p.setNombrePaciente(dto.getNombrePaciente());
        p.setRunMedico(dto.getRunMedico());
        p.setNombreMedico(dto.getNombreMedico());
        p.setFecha(dto.getFecha());
        p.setHoraDeAtencion(dto.getHoraDeAtencion());

        return mapToResponse(pedirHoraRepository.save(p), token);
    }

    public void eliminar(Long id) {
        pedirHoraRepository.deleteById(id);
    }

    private PedirHoraResponse mapToResponse(PedirHora pedirHora, String token) {

        var paciente = pacienteClient.getPacienteClient(pedirHora.getRunPaciente(), token);
        var medico = medicoClient.getMedicoClient(pedirHora.getRunMedico(), token);

        return PedirHoraResponse.builder()
                .id(pedirHora.getId())
                .paciente(paciente)
                .medico(medico)
                .fecha(pedirHora.getFecha())
                .horaDeAtencion(pedirHora.getHoraDeAtencion())  
                .atencion(pedirHora.getAtencion())
                .build();
    }
}