package com.example.ms_ficha.medica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builderpublic Object queMedicamentoEstaTomando(String queMedicamentoEstaTomando) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'queMedicamentoEstaTomando'");
    }
public class FichaMedicaResponse {
    private Long id;

    private PacienteResponse paciente;
    private MedicoResponse medico;
    private String procedimiento;
    private String odontograma;
    public Object getQueMedicamentoEstaTomando() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQueMedicamentoEstaTomando'");
    }
    public static Object builder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }
}

