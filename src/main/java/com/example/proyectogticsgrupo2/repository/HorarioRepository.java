package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.HorarioOcupadoDTO;
import com.example.proyectogticsgrupo2.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    @Query(nativeQuery = true, value = "select cast(c.inicio as time) as horario from cita c " +
            "where c.doctor_id_doctor = ?1 and cast(c.inicio as date) = ?2")
    List<HorarioOcupadoDTO> buscarHorariosOcupados(String idDoctor, LocalDate fecha);
}
