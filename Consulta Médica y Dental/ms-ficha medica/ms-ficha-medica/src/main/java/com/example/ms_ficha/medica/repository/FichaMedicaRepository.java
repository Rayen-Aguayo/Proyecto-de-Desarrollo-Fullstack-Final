package com.example.ms_ficha.medica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_ficha.medica.model.FichaMedica;

public interface FichaMedicaRepository extends JpaRepository<FichaMedica,Long> {

}
