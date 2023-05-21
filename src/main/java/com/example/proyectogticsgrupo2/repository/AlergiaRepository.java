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

}
