package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, String> {
    @Query(value = "Select * FROM cita WHERE doctor_id_doctor = ?1",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<Cita> BuscarPorDoctor(String id);

    @Query(value = "SELECT * FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor='09568265' and lower(concat(p.nombre,' ',p.apellidos)) like %?1%",
            nativeQuery = true)
    List<Cita> buscadorProximasCitas(String nombre);


    /* seccion aun en desarrollo :u (Noe)
    @Query(nativeQuery = true, value = "")
    List<Cita> buscarProximasCitas (String idPaciente);
    */

}
