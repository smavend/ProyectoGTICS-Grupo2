package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.CitaTemporal;
import com.example.proyectogticsgrupo2.entity.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaTemporalRepository extends JpaRepository<CitaTemporal, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `proyectogtics`.`cita_temporal` (`paciente_id_paciente`, `modalidad`, `sede_id_sede`, `especialidad_id_especialidad`) VALUES (?1, ?2, ?3, ?4);")
    void guardarModalidadSedeEspecialidad(String idPaciente, int modalidad, int idSede, int idEspecialidad);
}
