package com.example.ms_paciente.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class PacienteDTO {
    @NotBlank(message = "el run no puede estar vacio")
    private String runPaciente;

    @NotBlank(message = "el nombre no puede estar vacio")
    private String nombrePaciente;

    @NotBlank(message = "los datos del paciente no pueden esta vacios")
    private String datosDelPaciente;

    @NotNull(message = "la edad del paciente no puede estar vacio")
    @Min(value = 0, message = "la edad debe ser positiva")
    private Integer edadPaciente;

    private String alergias;
    private String enfermedad;
    private String queMedicamentoEstaTomando;

    @NotBlank(message = "El numero del telefono es obligatorio") 
    @Size(min = 9, max = 9, message = "El numero del telefono debe tener 9 digitos")
    private String nroTelefono;
}
