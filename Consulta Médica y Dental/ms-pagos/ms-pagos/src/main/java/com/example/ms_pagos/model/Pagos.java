package com.example.ms_pagos.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String runPaciente;    
    private String nombrePaciente;
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
