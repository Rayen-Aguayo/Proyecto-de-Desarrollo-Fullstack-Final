package com.example.ms_ficha.medica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fichaMedica")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String runPaciente;
    private String nombrePaciente;
    private String runMedico;
    private String nombreMedico;
    private String procedimiento;
    private String queMedicamentoEstaTomando;
    private String enfermedad;
    private String alergias;
    private String odontograma;
}
