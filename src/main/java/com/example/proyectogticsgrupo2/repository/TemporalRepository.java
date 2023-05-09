package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Temporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporalRepository extends JpaRepository<Temporal, Integer> {
}
