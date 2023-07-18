package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.HorarioDeDiaDTO;
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

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_lunes as inicio, disponibilidad_fin_lunes as fin, comida_inicio_lunes as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioLunes(String idDoctor);

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_martes as inicio, disponibilidad_fin_martes as fin, comida_inicio_martes as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioMartes(String idDoctor);

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_miercoles as inicio, disponibilidad_fin_miercoles as fin, comida_inicio_miercoles as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioMiercoles(String idDoctor);

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_jueves as inicio, disponibilidad_fin_jueves as fin, comida_inicio_jueves as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioJueves(String idDoctor);

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_viernes as inicio, disponibilidad_fin_viernes as fin, comida_inicio_viernes as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioViernes(String idDoctor);

    @Query(nativeQuery = true, value = "select disponibilidad_inicio_sabado as inicio, disponibilidad_fin_sabado as fin, comida_inicio_sabado as comidaInicio from horario h " +
            "inner join doctor d on (h.id_horario = d.horario_id_horario) " +
            "where d.id_doctor = ?1")
    HorarioDeDiaDTO buscarHorarioSabado(String idDoctor);

    @Query(nativeQuery = true, value = "select * from horario where id_horario=?1")
    Horario buscarHorarioPorDoctorId(int idHorario);
    @Query(nativeQuery = true, value = "select * from horario where id_horario=?1")
    Horario buscarHorarioPorDoctorIde(int idHorario);
}
