package com.example.ms_opinion.del.paciente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpinionPacienteResponse {
    private Long id;
    private PacienteResponse paciente;
    private MedicoResponse medico;
    private Integer atencionMedico;
    private String expliqueSuPuntuacion;
    private String explicacionTratamiento;
    private String comentarioMejora;
    private Integer puntuacionMedico;
}
