package com.example.ms_facturacion.y.presupuesto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FacturacionYPresupuestoDTO {

    @NotNull(message = "El presupuesto es obligatorio")
    private Double presupuesto;
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombrePaciente;
    @NotBlank(message = "El run del paciente es obligatorio")
    private String runPaciente;
    @NotBlank(message = "El nombre del medico es obligatorio")
    private String nombreMedico;
    @NotBlank(message = "El run del medico es obligatorio")
    private String runMedico;
    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;
    @NotNull(message = "Los dias de duracion son obligatorios")
    private Integer diasDuracion;
    @NotBlank(message = "La gestion de pagos es obligatoria")
    private String gestionPagos;
}
