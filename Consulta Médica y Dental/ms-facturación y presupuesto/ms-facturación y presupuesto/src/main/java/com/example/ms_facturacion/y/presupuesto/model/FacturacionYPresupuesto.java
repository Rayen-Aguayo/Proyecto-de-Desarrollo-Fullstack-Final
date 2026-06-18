package com.example.ms_facturacion.y.presupuesto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "facturacion_Y_presupuesto")
public class FacturacionYPresupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double presupuesto;
    private String nombrePaciente;
    private String runPaciente;
    private String nombreMedico;
    private String runMedico;
    private String tratamiento;
    private Integer diasDuracion;
    private String gestionPagos;

}


