package com.decoplants.sistema_web.services;

import com.decoplants.sistema_web.models.Incidencia;
import com.decoplants.sistema_web.repositories.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    // Lista las incidencias de la más reciente a la más antigua
    public List<Incidencia> listarTodas() {
        return incidenciaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro"));
    }

    public Incidencia obtenerPorId(Integer id) {
        return incidenciaRepository.findById(id).orElse(null);
    }

    public Incidencia guardar(Incidencia incidencia) {
        // Si es una incidencia nueva, le asignamos la fecha y estado inicial
        if (incidencia.getIdIncidencia() == null) {
            incidencia.setFechaRegistro(LocalDateTime.now());
            incidencia.setEstado("Abierto");
        }
        return incidenciaRepository.save(incidencia);
    }
}