package com.decoplants.sistema_web.services;

import com.decoplants.sistema_web.models.Categoria;
import com.decoplants.sistema_web.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Método para llenar los select/dropdowns en los formularios
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}