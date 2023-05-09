package com.example.proyectogticsgrupo2.repository;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.proyectogticsgrupo2.dto.MensajeDatosDto;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    @Query(nativeQuery = true, value = "select * from paciente where administrativo_id_administrativo = ?1 and estado!=0")
    List<Paciente> buscarPorIdAdministrativo(String id);

    @Query(nativeQuery = true, value = "SELECT m.*, doctor.nombre as 'nombreemisor' FROM mensaje m inner join doctor on (doctor.id_doctor=m.id_emisor) where id_receptor=?1")
    List<MensajeDatosDto> obtenerMensajeDatos(String id);


    @Query(nativeQuery = true, value = "INSERT INTO proyectogtics.paciente (id_paciente, nombre, apellidos,estado, seguro_id_seguro, telefono, administrativo_id_administrativo, correo, foto,direccion, distrito_id_distrito) " +
            "VALUES (?1, ?2, ?3,?4 ?5, " +
            "?6, ?7, ?8, ?9, ?10, ?11")
    void guardarPaciente (String dni, String nombre, String apellido, int estado, int numseguro, String telefono, String numadmin,
                          String correo, Byte foto, String direccion, int numdistrito);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE paciente SET correo = ?1, direccion = ?2, distrito_id_distrito = ?3 WHERE (id_paciente = ?4)")
    void actualizarPaciente(String correo, String direccion, int idDistrito, String idPaciente);
    List<Paciente> findByIdPaciente(Integer id);

}
