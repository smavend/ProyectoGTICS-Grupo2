package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    @Query(nativeQuery = true, value = "select * from paciente where administrativo_id_administrativo = ?1")
    List<Paciente> buscarPorIdAdministrativo(String id);
}
