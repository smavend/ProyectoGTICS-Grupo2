package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Especialidad;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM proyectogtics.sede where clinica_id_clinica=?1")
    List<Sede> listarSedes(int id);
}
