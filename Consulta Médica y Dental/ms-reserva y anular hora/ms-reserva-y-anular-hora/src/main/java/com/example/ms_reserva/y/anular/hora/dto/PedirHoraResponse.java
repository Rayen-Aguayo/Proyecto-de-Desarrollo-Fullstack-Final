package com.example.ms_reserva.y.anular.hora.dto;

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
public class PedirHoraResponse {
    
    private Long id;
    
   private PacienteResponse paciente;
    private MedicoResponse medico;
    private LocalDate fecha;
    private LocalTime horaDeAtencion;  

    private String atencion;
}
