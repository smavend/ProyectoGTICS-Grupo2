package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Alergia;
import com.example.proyectogticsgrupo2.entity.AlergiaXPaciente;
import com.example.proyectogticsgrupo2.entity.AlergiaXPacienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlergiaPacienteRepository extends JpaRepository<AlergiaXPacienteId, Integer> {
    List<AlergiaXPacienteId> findByPacienteIdPaciente(String id);
}
