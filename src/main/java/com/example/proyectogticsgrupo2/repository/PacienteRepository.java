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
import java.util.Optional;

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
    Optional<Paciente> findByIdPaciente(String id);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE c , p, ct\n" +
            "FROM proyectogtics.pago p  left join proyectogtics.cita c on (p.cita_id_cita = c.id_cita) \n" +
            "left join proyectogtics.cuestionario_x_cita ct on (ct.cita_id_cita = p.cita_id_cita)\n" +
            "WHERE (c.inicio<now() and p.estado_pago='0')")
    void anularCitaNoCancelada();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE paciente SET estado = ?1 WHERE (idPaciente = ?2)")
    void cambiarEstado(int estado, String idPaciente);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`paciente` SET `foto` = null WHERE (`id_paciente` = ?1)")
    void quitarFoto(String idPaciente);

    Paciente findByCorreo(String correo);


    @Query(nativeQuery = true, value = "select * from paciente where estado = ?1")
    List<Paciente> buscarPorEstado(int i);
}
