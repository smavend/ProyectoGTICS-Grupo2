package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Clinica;
import com.example.proyectogticsgrupo2.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {


    @Query(nativeQuery = true, value = "select * from especialidad where nombre = ?1")
    Especialidad findByNombre(String especialidad);
    List<Especialidad> findByIdEspecialidad(int idEspecialidad);
}

