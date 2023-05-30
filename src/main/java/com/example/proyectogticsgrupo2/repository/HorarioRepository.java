package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.DiasDisponiblesDTO;
import com.example.proyectogticsgrupo2.dto.HorariosDisponiblesDTO;
import com.example.proyectogticsgrupo2.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HorarioRepository extends JpaRepository<Horario, String> {

    @Query(nativeQuery = true, value = "select addtime(h.disponibilidad_inicio, sec_to_time(d.duracion_cita_minutos*60)) as horario1,\n" +
            "            addtime(h.comida_inicio, sec_to_time(d.duracion_cita_minutos*60)) as horario2, \n" +
            "            addtime(h.comida_inicio, sec_to_time(d.duracion_cita_minutos*60*3)) as horario3 from horario h \n" +
            "            inner join doctor d on (d.horario_id_horario = h.id_horario) \n" +
            "            where d.id_doctor = ?1")
    HorariosDisponiblesDTO buscarHorariosDisponibles(String idDoctor);
}
