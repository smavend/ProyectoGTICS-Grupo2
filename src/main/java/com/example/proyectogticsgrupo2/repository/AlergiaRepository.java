package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.AlergiasPacienteDTO;
import com.example.proyectogticsgrupo2.entity.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Integer> {
    @Query(nativeQuery = true, value = "select * from alergias where paciente_id_paciente=?1")
    List<Alergia> buscarPorPacienteId(String id);
    @Query(value = "SELECT a.nombre FROM proyectogtics.alergias a inner join proyectogtics.alergia_x_paciente axp on (a.id_alergia=axp.alergias_id_alergia) where axp.paciente_id_paciente=?1",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<AlergiasPacienteDTO> listarAlergiasPaciente(String id);

}
