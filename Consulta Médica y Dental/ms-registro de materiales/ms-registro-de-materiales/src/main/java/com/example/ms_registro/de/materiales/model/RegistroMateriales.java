package com.example.ms_registro.de.materiales.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RegistroMateriales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroMateriales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidadProductos;
    private String nombresProductos;
    private LocalDate fechaCaducidadProductos;

}
