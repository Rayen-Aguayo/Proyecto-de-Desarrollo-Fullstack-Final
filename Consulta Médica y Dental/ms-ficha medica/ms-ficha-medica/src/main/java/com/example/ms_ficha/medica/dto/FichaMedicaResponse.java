package com.example.ms_ficha.medica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FichaMedicaResponse {
    private Long id;

    private PacienteResponse paciente;
    private MedicoResponse medico;
    private String procedimiento;
    private String queMedicamentoEstaTomando;
    private String enfermedad;
    private String alergias;
    private String odontograma;
}

