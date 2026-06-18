package com.example.ms_registro.de.atenciones.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAtencionesDTO {
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nompaciente; 
    @NotBlank(message = "El run del paciente es obligatorio")
    private String runpaciente; 
    @NotBlank(message = "El nombre del médico es obligatorio")
    private String nommedico;
    @NotBlank(message = "El run del médico es obligatorio")
    private String runmedico;
    @NotNull(message = "El total es obligatorio")
    private Double total;
    @NotNull(message = "El id del pago es obligatorio")
    private Integer idPago;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
    @NotBlank(message = "El tratamiento realizado es obligatorio")
    private String tratamientoRealizado;

}
