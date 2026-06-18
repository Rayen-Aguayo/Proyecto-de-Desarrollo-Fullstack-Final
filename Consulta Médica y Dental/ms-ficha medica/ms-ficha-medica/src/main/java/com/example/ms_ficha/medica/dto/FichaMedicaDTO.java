package com.example.ms_ficha.medica.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaMedicaDTO {
    @NotBlank(message = "El run no puede estar vacio")
    private String runPaciente;

    @NotBlank(message = "El Nombre del Paciente no puede esta vacio")
    private String nombrePaciente;

    @NotBlank(message = "El run no puede estar vacio")
    private String runMedico;

    @NotBlank(message = "El Nombre del Medico no puede estar vacio")
    private String nombreMedico;

    @NotBlank(message = "El procedimiento no puede estar vacio")
    private String procedimiento;

    @NotBlank(message = "El medicamento que toma el paciente es obligatorio escribirlo")
    private String queMedicamentoEstaTomando;

    @NotBlank(message = "La enfermadad del paciente es obligatorio para cualquier procedimieno")
    private String enfermedad;
    
    @NotBlank(message = "Las alergias es obligatorio para continuar cualquier procedimiento")
    private String alergias;

    @NotBlank(message = "El odontograma es obligatorio para saber que dientes estan malos")
    private String odontograma;
}
