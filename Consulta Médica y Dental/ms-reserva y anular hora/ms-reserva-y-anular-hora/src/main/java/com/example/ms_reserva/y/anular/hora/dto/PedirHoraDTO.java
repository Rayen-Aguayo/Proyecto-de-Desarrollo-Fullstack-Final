package com.example.ms_reservar.y.anular.hora.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PedirHoraDTO {

    @NotBlank(message = "el run del paciente no puede estar vacio")
    private String runPaciente;
    @NotBlank(message = "el nombre del paciente no puede estar vacio")
    private String nombrePaciente;
    @NotBlank(message = "el run del medico no puede estar vacio")
    private String runMedico;
    @NotBlank(message = "el nombre del medico no puede estar vacio")
    private String nombreMedico;
    @NotNull(message = "la fecha no puede estar vacia")
    private LocalDate fecha;
    @NotNull(message = "la hora de atencion no puede estar vacio")
    private LocalTime horaDeAtencion;  
    @NotBlank(message = "la atencion que va a recibir el paciente no puede estar vacio")
    private String atencion;
}
