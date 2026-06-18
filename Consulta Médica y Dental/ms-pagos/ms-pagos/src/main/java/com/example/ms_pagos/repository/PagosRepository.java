package com.example.ms_pagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_pagos.model.Pagos;

public interface PagosRepository extends JpaRepository<Pagos, Long> {

}
