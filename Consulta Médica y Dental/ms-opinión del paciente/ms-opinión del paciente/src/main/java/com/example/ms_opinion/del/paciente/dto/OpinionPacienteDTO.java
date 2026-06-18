package com.example.ms_opinion.del.paciente.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class OpinionPacienteDTO {
    @NotBlank(message = "El run del paciente es obligatorio")
    private String runPaciente;
    @NotBlank(message = "El run del medico es obligatorio")
    private String runMedico;
    @NotBlank(message = "El nombre del médico es obligatorio")
    private String nombreMedico;
    @NotNull(message = "La puntuación de atención es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Integer atencionMedico;
    @NotBlank(message = "Explique porque escribio esto es obligatorio para mejorar la experiencia")
    private String expliqueSuPuntuacion;
    @NotBlank(message = "La explicación del tratamiento es obligatoria")
    private String explicacionTratamiento;
    @NotBlank(message = "Esto es obligatorio para mejorar la atencion")
    private String comentarioMejora;
    @NotNull(message = "La puntuación del médico es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Integer puntuacionMedico;
}
