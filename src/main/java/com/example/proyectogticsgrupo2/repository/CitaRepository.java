package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.ListaBuscadorDoctor;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, String> {

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor=?1",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorProxCitas(String id);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor=?1",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorListaPacientes(String id);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor= ?1 and lower(concat(p.nombre,' ',p.apellidos)) like %?2%", nativeQuery = true)
    List<ListaBuscadorDoctor> buscadorProximasCitas(String id,String nombre);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor= ?1 and lower(concat(p.nombre,' ',p.apellidos)) like %?2%", nativeQuery = true)
    List<ListaBuscadorDoctor> buscadorPaciente(String id,String nombre);

    @Query(nativeQuery = true, value = "select c.* from cita c \n" +
            "inner join paciente p on (c.paciente_id_paciente = p.id_paciente) \n" +
            "where NOW() <= c.inicio and p.id_paciente = ?1")
    List<Cita> buscarProximasCitas (String idPaciente);
}