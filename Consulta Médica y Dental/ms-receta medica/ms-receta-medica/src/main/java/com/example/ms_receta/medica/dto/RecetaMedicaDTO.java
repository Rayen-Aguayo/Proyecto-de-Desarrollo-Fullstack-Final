package com.example.ms_receta.medica.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class RecetaMedicaDTO {
    @NotBlank(message = "El nombre del medicameto es obligatorio")
    private String nomMedicamento;

    @NotNull(message = "Los dia que tiene que tomar el medicamento no puede estar vacio")
    private Integer diasTomarMedicamento;

    @NotNull(message = "La fecha de inicio de la receta no puede estar vacia")
    private LocalDate inicioReceta;

    @NotBlank(message = "El nombre del medico es obligatorio")
    private String nomMedico;

    @NotBlank(message = "El run del medico es obligatorio")
    private String runMedico;

    @NotNull(message = "La cantidad que tiene que tomar por dia no puede estar vacia")
    private Integer cantTomarDia;

    @NotBlank(message = "La firma del medico es obligatorio")
    private String firmaMedico;
}
