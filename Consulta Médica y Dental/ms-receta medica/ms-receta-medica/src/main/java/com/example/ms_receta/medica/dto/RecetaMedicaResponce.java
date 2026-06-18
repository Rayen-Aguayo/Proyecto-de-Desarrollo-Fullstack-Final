package com.example.ms_receta.medica.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecetaMedicaResponce {

    private Long id;
    private String nomMedicamento;
    private Integer diasTomarMedicamento;
    private LocalDate inicioReceta;
    private MedicoResponse medico;
    private Integer cantTomarDia;
}