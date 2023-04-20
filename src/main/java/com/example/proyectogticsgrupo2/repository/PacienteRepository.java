package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.MensajeDatosDto;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    @Query(nativeQuery = true, value = "select * from paciente where administrativo_id_administrativo = ?1 and estado!=0")
    List<Paciente> buscarPorIdAdministrativo(String id);

    @Query(nativeQuery = true, value = "SELECT m.*, doctor.nombre as 'nombreemisor' FROM mensaje m inner join doctor on (doctor.id_doctor=m.id_emisor) where id_receptor=?1")
    List<MensajeDatosDto> obtenerMensajeDatos(String id);
}
