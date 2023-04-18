package com.example.proyectogticsgrupo2.repository;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String>{
}
