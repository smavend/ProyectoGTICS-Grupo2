package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Clinica;
import com.example.proyectogticsgrupo2.entity.Sede;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Integer> {
    @Query(nativeQuery = true, value = "select * from clinica where id_clinica = ?1")
    Clinica buscarClinicaPorID(int ID);

    @Query(nativeQuery = true, value = "select * from clinica where nombre = ?1")
    Clinica buscarClinicaPorNombre(String clinicaId);

//
//    @Transactional
//    @Modifying
//    @Query(value = "INSERT INTO clinica (nombre, color,tyc, telefono, correo) VALUES (:otraClinica, '#FFFFFF','terminosX', '0000000000', 'contacto@clinica.com')", nativeQuery = true)
//    void insertarClinica(@Param("otraClinica") String otraClinica);
}
