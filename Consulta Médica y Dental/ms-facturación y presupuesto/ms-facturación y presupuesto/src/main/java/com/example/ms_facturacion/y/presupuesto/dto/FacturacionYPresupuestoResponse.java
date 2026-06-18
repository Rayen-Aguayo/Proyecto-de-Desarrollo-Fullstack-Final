package com.example.ms_facturacion.y.presupuesto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturacionYPresupuestoResponse {
    
    private Long id;

    private Double presupuesto;
    private PacienteResponse paciente;
    private MedicoResponse medico;
    private String tratamiento;
    private Integer diasDuracion;
    private String gestionPagos;
}
