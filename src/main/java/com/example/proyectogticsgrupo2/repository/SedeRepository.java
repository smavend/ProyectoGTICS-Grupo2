package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Sede;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {

    @Query(nativeQuery = true, value = "select * from sede where id_sede = ?1")
    Sede buscarPorSedeId(String id);

    @Query(nativeQuery = true, value = "select * from sede where clinica_id_clinica = ?1")
    Sede buscarPorClinicaId(String id);
    @Query(nativeQuery = true, value = "select * from sede where clinica_id_clinica = ?1")
    List<Sede> EncontrarListaPorId(int clinicaId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sede (nombre, clinica_id_clinica) VALUES (:otraSede,:clinica_id)", nativeQuery = true)
    void insertarSede(@Param("otraSede") String otraSede, @Param("clinica_id") int clinica_id);


    @Query(nativeQuery = true, value = "select * from sede where nombre = ?1")
    Sede buscarPorNombreDeSede(String otraSede);

}
