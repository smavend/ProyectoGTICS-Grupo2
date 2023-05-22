package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query(nativeQuery = true, value = "insert into proyectogtics.doctor (id_doctor, nombre,apellidos, estado, especialidad_id_especialidad, sede_id_sede, correo) \n" +
            "values (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    void guardarDoctor (String dni, String nombre, String apellido, String estado, int especialidad, int sede, String correo);

    @Query(nativeQuery = true, value = "SELECT * FROM proyectogtics.doctor where sede_id_sede=?1")
    List<Doctor> listDoctorSede(int idSede);

    @Query(nativeQuery = true, value = "SELECT * FROM proyectogtics.doctor where sede_id_sede=?1 and especialidad_id_especialidad=?2")
    List<Doctor> listDoctorSedeEspecialidad(int idSede, int idEspecialidad);

    @Query(nativeQuery = true, value = "SELECT sede_id_sede FROM proyectogtics.doctor where id_doctor=?1")
    int buscarIdSedeDoctor(String idDoctor);

    Doctor findByCorreo(String correo);
}
