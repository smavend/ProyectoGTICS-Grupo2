package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.CuestionarioPorCita;
import com.example.proyectogticsgrupo2.entity.CuestionarioPorCitaID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuestionarioPorCitaRepository extends JpaRepository<CuestionarioPorCita, CuestionarioPorCitaID> {

    @Query(nativeQuery = true, value = "select x.* from cuestionario_x_cita x \n" +
            "inner join cita c on (x.cita_id_cita = c.id_cita) \n" +
            "inner join paciente p on (p.id_paciente = c.paciente_id_paciente) " +
            "WHERE p.id_paciente = ?1")
    List<CuestionarioPorCita> buscarPorPaciente(String idPaciente);
}
