package com.example.ms_medico.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoDTO {
    
    @NotBlank(message =  "El run no puede estar vacio")
    private String runMedico;

    @NotBlank(message =  "El nombre no puede estar vacio")
    private String nombreMedico;

    @NotNull(message = "La edad del medico no puede estar vacio")
    @Min(value = 0, message = "La edad debe ser positiva")
    private Integer edad;

    @NotBlank(message = "El numero del telefono es obligatorio") 
    @Size(min = 9, max = 9, message = "El numero del telefono debe tener 9 digitos")
    private String nroTelefono;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotBlank(message =  "El medico debe tener su firma")
    private String firmaMedico;
}
