package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Administrador;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Paciente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO administrador (id_administrador, nombre, apellidos, estado, sede_id_sede, correo) VALUES (:dni, :nombres, :apellidos, 1, :sedenuevaId, :correoUser)", nativeQuery = true)
    void insertarAdministrador(@Param("dni") String dni, @Param("nombres") String nombres, @Param("apellidos") String apellidos, @Param("sedenuevaId") int sedenuevaId, @Param("correoUser") String correoUser);
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where administrativo_id_administrativo = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorAdministrativoId(String id);


    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where sede_id_sede = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorSedeId(String id);
    @Query(nativeQuery = true, value = "select sede_id_sede from administrador where id_administrador = ?1")
    int obteneSedePorAdministradorId(String id);

    @Query("SELECT a.sede.idSede FROM Administrador a")
    List<Integer> findSedesConAdministrador();

    Administrador findByCorreo(String correo);
}
