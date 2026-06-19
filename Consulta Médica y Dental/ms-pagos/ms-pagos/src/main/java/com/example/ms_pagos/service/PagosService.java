package com.example.ms_pagos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_pagos.client.PacienteClient;
import com.example.ms_pagos.dto.PagosDTO;
import com.example.ms_pagos.dto.PagosResponse;
import com.example.ms_pagos.model.Pagos;
import com.example.ms_pagos.repository.PagosRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagosService {
    
    private final PagosRepository pagosRepository;
    private final PacienteClient pacienteClient;

    public PagosResponse  crear(PagosDTO dto, String token) {

        log.info("registrar pago", keyValue("run paciente", dto.getRunPaciente()));

        var pacienteRun = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);
        
        if (pacienteRun == null) {
            throw new RuntimeException("El paciente no existe");
        }

            Pagos pagos = new Pagos(null, 
                dto.getRunPaciente(), 
                dto.getNombrePaciente(), 
                dto.getFecha(),
                dto.getHora(),
                dto.getMetodoPago(),
                dto.getNroBoleta(),
                dto.getRegistroFacturacion(),
                dto.getNeto(),
                dto.getIva(),
                dto.getTotal(),
                dto.getEstado());
                    
        return mapToResponse(pagos, token);
    }

    public List<PagosResponse> listar(String token) {

        return pagosRepository.findAll()
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    public PagosResponse obtener(Long id, String token) {

        Pagos pagos = pagosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("el pago no existe"));

        return mapToResponse(pagos, token);
    }

    public PagosResponse actualizar(Long id, PagosDTO dto, String token) {

        var paciente = pacienteClient.getPacienteClient(dto.getRunPaciente(), token);

        if (paciente == null) {
            throw new RuntimeException("El paciente no existe");
        }

        Pagos p = pagosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));

        p.setEstado(dto.getEstado());

        return mapToResponse(pagosRepository.save(p), token);
    }


    private PagosResponse mapToResponse(Pagos pagos, String token) {

        var paciente = pacienteClient.getPacienteClient(pagos.getRunPaciente(), token);
        return PagosResponse.builder()
                .id(pagos.getId())
                .paciente(paciente)
                .fecha(pagos.getFecha())
                .hora(pagos.getHora())
                .metodoPago(pagos.getMetodoPago())
                .nroBoleta(pagos.getNroBoleta())
                .registroFacturacion(pagos.getRegistroFacturacion())
                .neto(pagos.getNeto())
                .iva(pagos.getIva())
                .total(pagos.getTotal())
                .estado(pagos.getEstado())
                .build();
    }
}