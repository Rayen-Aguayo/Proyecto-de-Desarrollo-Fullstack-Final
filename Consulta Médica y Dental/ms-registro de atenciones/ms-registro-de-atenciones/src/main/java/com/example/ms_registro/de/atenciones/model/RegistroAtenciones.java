package com.example.ms_registro.de.atenciones.model;

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
public class RegistroAtenciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nompaciente; 
    private String runpaciente; 
    private String nommedico;
    private String runmedico;
    private Double total;
    private Integer idPago;
    private LocalDate fecha;
    private LocalTime hora;
    private String tratamientoRealizado;

}

