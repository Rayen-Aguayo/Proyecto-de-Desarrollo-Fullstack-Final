package com.example.ms_pagos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagosDTO {
    @NotBlank(message = "El run del paciente es obligatorio")
    private String runPaciente;  
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombrePaciente;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
    @NotNull(message = "El número de boleta es obligatorio")
    private Integer nroBoleta;
    @NotBlank(message = "El registro de facturación es obligatorio")
    private String registroFacturacion;
    @NotNull(message = "El neto es obligatorio")
    private Double neto;
    @NotNull(message = "El IVA es obligatorio")
    private Double iva;
    private Double total; 
    @NotBlank(message = "El estado es obligatorio")
    private String estado; 
}