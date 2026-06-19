package com.example.ms_medico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medico")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Medico {
    @Id
    private String runMedico;

    private String nombreMedico;
    private Integer edad;
    private String nroTelefono;
    private String especialidad;
    private String firmaMedico;
    public String getRunPaciente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRunPaciente'");
    }
}
