package com.example.REPOSITORY;

import com.example.MODEL.Categoria;
import com.example.MODEL.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}

