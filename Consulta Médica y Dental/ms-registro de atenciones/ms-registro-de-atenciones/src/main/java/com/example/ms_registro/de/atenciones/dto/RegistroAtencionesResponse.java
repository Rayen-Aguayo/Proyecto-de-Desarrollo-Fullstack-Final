package com.example.ms_registro.de.atenciones.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroAtencionesResponse {
    private Long id;
    private PacienteResponse paciente; 
    private MedicoResponse medico;
    private PagosResponse pago;
    private LocalDate fecha;
    private LocalTime hora;
    private String tratamientoRealizado;
}
