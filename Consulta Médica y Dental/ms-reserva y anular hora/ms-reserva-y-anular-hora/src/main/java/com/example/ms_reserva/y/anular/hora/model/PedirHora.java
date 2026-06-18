package com.example.ms_reserva.y.anular.hora.model;

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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedir_hora")
public class PedirHora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String runPaciente;
    private String nombrePaciente;
    private String runMedico;
    private String nombreMedico;
    private LocalDate fecha;
    private LocalTime horaDeAtencion;  
    private String atencion;

}
