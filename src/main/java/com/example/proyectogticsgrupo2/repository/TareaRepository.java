package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Tarea;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {
}
