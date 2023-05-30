package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Stylevistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StylevistasRepository extends JpaRepository<Stylevistas, Integer> {
    // Aquí puedes definir métodos de consulta personalizados si es necesario.
}