package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSedeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativoPorEspecialidadPorSedeRepository extends JpaRepository<AdministrativoPorEspecialidadPorSede, AdministrativoPorEspecialidadPorSedeId> {
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where administrativo_id_administrativo = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorAdministrativoId(String id);
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where sede_id_sede = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorSedeId(String id);
    @Query(nativeQuery = true, value = "select sede_id_sede from administrador where id_administrador = ?1")
    int obteneSedePorAdministradorId(String id);
}
