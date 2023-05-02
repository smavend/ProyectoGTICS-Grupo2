package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.PacientePorConsentimiento;
import com.example.proyectogticsgrupo2.entity.PacientePorConsentimientoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacientePorConsentimientoRepository extends JpaRepository<PacientePorConsentimiento, PacientePorConsentimientoId> {
    List<PacientePorConsentimiento> findByIdIdPaciente(String idPaciente);
}
