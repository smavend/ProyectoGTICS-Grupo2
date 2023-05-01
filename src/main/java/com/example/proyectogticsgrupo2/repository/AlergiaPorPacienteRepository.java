package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.AlergiaPorPaciente;
import com.example.proyectogticsgrupo2.entity.AlergiaPorPacienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlergiaPorPacienteRepository extends JpaRepository<AlergiaPorPaciente, AlergiaPorPacienteId> {
}
