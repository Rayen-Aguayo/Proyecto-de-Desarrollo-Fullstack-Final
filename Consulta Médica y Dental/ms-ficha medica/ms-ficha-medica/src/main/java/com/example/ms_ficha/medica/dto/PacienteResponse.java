package com.example.ms_ficha.medica.dto;

import lombok.Data;

@Data
public class PacienteResponse {
    private String runPaciente;
    private String nombrePaciente;
    private String alergias;
    private String enfermedad;
    private String queMedicamentoEstaTomando;

}
