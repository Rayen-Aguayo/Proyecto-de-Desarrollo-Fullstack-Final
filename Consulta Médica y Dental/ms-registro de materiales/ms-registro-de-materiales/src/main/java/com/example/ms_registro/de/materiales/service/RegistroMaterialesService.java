package com.example.ms_registro.de.materiales.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_registro.de.materiales.dto.RegistroMaterialesDTO;
import com.example.ms_registro.de.materiales.model.RegistroMateriales;
import com.example.ms_registro.de.materiales.repository.RegistroMaterialesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistroMaterialesService {
    private final RegistroMaterialesRepository Repository;

    public RegistroMateriales crear(RegistroMaterialesDTO dto) {
        log.info("Crear registro de materiales", keyValue("cantidad", dto.getCantidadProductos()));

        RegistroMateriales r = new RegistroMateriales(null,
            dto.getCantidadProductos(), 
            dto.getNombresProductos(),
            dto.getFechaCaducidadProductos()
        );
 
        return Repository.save(r);
    }

    public List<RegistroMateriales> listar() {
        log.info("Listar registros de materiales");
        return Repository.findAll();
    }

    public RegistroMateriales obtener(Long id) {
        log.info("Obtener registro de materiales", keyValue("id", id));

        return Repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("registro de materiales no encontrado"));
    }

    public RegistroMateriales actualizar(Long id, RegistroMaterialesDTO dto) {
        log.info("Actualizar registro de materiales", keyValue("id", id));

        RegistroMateriales r = obtener(id);
        r.setCantidadProductos(dto.getCantidadProductos());
        r.setNombresProductos(dto.getNombresProductos());
        r.setFechaCaducidadProductos(dto.getFechaCaducidadProductos());

        return Repository.save(r);
    }

    public void eliminar(Long id) {
        log.warn("Eliminar registro de materiales", keyValue("id", id));
        Repository.deleteById(id);
    }
}
