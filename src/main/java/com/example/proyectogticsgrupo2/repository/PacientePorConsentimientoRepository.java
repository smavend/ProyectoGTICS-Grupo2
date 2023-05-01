package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.PacientePorConsentimiento;
import com.example.proyectogticsgrupo2.entity.PacientePorConsentimientoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacientePorConsentimientoRepository extends JpaRepository<PacientePorConsentimiento, PacientePorConsentimientoID> {
}
