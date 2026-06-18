package com.example.ms_registro.de.atenciones.dto;

import lombok.Data;

@Data
public class PagosResponse {
    private Long id;
    private Double total; 
    private String estado; 
}
