package com.example.ms_registro.de.materiales.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroMaterialesDTO {
    
    @NotNull(message = "La cantidad de productos es obligatoria")
    private Integer cantidadProductos;
    @NotBlank(message = "Los nombres de los productos son obligatorios")
    private String nombresProductos;
    @NotNull(message = "La fecha de caducidad de los productos es obligatoria")
    private LocalDate fechaCaducidadProductos;
}
