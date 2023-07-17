package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSedeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministrativoPorEspecialidadPorSedeRepository extends JpaRepository<AdministrativoPorEspecialidadPorSede, AdministrativoPorEspecialidadPorSedeId> {
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where administrativo_id_administrativo = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorAdministrativoId(String id);

    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where sede_id_sede = ?1")
    List<AdministrativoPorEspecialidadPorSede> buscarPorSedeId(String id);

    @Query(nativeQuery = true, value = "select sede_id_sede from administrador where id_administrador = ?1")
    int obteneSedePorAdministradorId(String id);

    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where administrativo_id_administrativo = ?1")
    List<AdministrativoPorEspecialidadPorSede> buscarXAdministrativoId(String id);




    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sede_x_especialidad_x_administrativo (sede_id_sede, especialidad_id_especialidad, administrativo_id_administrativo, torre, piso, precio_cita) VALUES (:idSede,:especialidad,:dni,:torre,:piso,0.0)", nativeQuery = true)
    void insertarTablaAdministrativoXEspecialidadXSede(@Param("idSede")int idSede, @Param("dni") String dni, @Param("especialidad") String especialidad, @Param("torre") String torre, @Param("piso") String piso);

    @Query(nativeQuery = true, value = "SELECT * FROM sede_x_especialidad_x_administrativo where " +
            "sede_id_sede = ?1 and especialidad_id_especialidad = ?2")
    AdministrativoPorEspecialidadPorSede buscarPorSedeYEspecialidad(Integer idSede, Integer idEspecialidad);

    AdministrativoPorEspecialidadPorSede findBySedeIdIdSedeAndEspecialidadIdIdEspecialidad(int idSede, int idEspecialidad);
}
