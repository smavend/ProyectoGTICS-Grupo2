package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Clinica;
import com.example.proyectogticsgrupo2.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {


    @Query(nativeQuery = true, value = "select * from especialidad where nombre = ?1")
    Especialidad findByNombre(String especialidad);

    @Query(nativeQuery = true, value = "select esp.* from especialidad esp \n" +
            "inner join sede_x_especialidad_x_administrativo m on (esp.id_especialidad = m.especialidad_id_especialidad) \n" +
            "where m.sede_id_sede = ?1")
    List<Especialidad> buscarPorSede(int idSede);

    @Query(nativeQuery = true, value = "select esp.* from especialidad esp \n" +
            "inner join sede_x_especialidad_x_administrativo m on (esp.id_especialidad = m.especialidad_id_especialidad) \n" +
            "where m.sede_id_sede = ?1 and esp.es_examen = 0;")
    List<Especialidad> buscarVirtualesPorSede(int idSede);


    @Query(nativeQuery = true, value = "SELECT * FROM especialidad e " +
            "WHERE e.es_examen = 0")
    List<Especialidad> buscarEspecialidadesVirtuales();
}