package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {

}
