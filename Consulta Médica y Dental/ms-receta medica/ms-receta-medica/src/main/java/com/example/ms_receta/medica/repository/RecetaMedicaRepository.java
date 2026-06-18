package com.example.ms_receta.medica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_receta.medica.model.RecetaMedica;

public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long>{

}