package com.example.ms_paciente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    private String runPaciente;

    private String nombrePaciente;

    private String datosDelPaciente;

    private Integer edadPaciente;

    private String alergias;
    private String enfermedad;
    private String queMedicamentoEstaTomando;

    private String nroTelefono;

}
