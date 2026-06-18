package com.example.ms_paciente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_paciente.model.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, String> {

}
