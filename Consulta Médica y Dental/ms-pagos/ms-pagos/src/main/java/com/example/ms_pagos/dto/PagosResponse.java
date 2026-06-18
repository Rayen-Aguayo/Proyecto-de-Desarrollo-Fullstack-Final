package com.example.ms_pagos.dto;


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
public class PagosResponse {

    private Long id;
    
    private PacienteResponse paciente;  
    private LocalDate fecha;
    private LocalTime hora;
    private String metodoPago;
    private Integer nroBoleta;
    private String registroFacturacion;
    private Double neto;
    private Double iva;
    private Double total; 
    private String estado; 

    
}
