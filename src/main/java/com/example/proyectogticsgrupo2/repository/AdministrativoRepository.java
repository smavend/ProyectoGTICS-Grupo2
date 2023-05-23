package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Administrativo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativoRepository extends JpaRepository<Administrativo, String> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO administrativo (id_administrativo, nombre, apellidos, estado, correo) VALUES (:dni,:nombres,:apellidos,1, 'administrativoX@clinica.com')", nativeQuery = true)
    void insertarAdministrativo(@Param("dni") String dni, @Param("nombres") String nombres, @Param("apellidos") String apellidos);

    Administrativo findByCorreo(String correo);
}
