package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query(nativeQuery = true, value = "insert into proyectogtics.doctor (id_doctor, nombre,apellidos, estado, especialidad_id_especialidad, sede_id_sede, correo) \n" +
            "values (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    void guardarDoctor (String dni, String nombre, String apellido, String estado, int especialidad, int sede, String correo);

    // Flujo paciente -----------------------------------------
    @Query(nativeQuery = true, value = "SELECT * FROM doctor where sede_id_sede=?1 limit ?2, ?3")
    List<Doctor> buscarDoctorSedePaginado(int idSede, int paginas, int cantidad);

    @Query(nativeQuery = true, value = "select count(doctor.id_doctor) from doctor where sede_id_sede = ?1")
    int numDoctoresSede(int idSede);

    @Query(nativeQuery = true, value = "select * from doctor d \n" +
            "where sede_id_sede = ?1 and especialidad_id_especialidad = ?2 limit ?3, ?4")
    List<Doctor> buscarDoctorSedeEspecialidadPaginado(int idSede, int idEspecialidad, int paginas, int cantidad);

    @Query(nativeQuery = true, value = "select count(doctor.id_doctor) from doctor where sede_id_sede = ?1 and especialidad_id_especialidad = ?2")
    int numDoctoresSedeEspecialidad(int idSede, int idEspecialidad);

    List<Doctor> findBySede_IdSedeAndEspecialidad_IdEspecialidad(int idSede, int idEspecialidad);

    @Query(nativeQuery = true, value = "select d.* from doctor d " +
            "inner join especialidad e on (d.especialidad_id_especialidad = e.id_especialidad) " +
            "where d.especialidad_id_especialidad = ?2 and d.sede_id_sede = ?1 and e.es_examen = 0")
    List<Doctor> buscarVirtualesPorSedeYEspecialidad(int idSede, int idEspecialidad);

    // ---------------------------------------------------------

    @Query(nativeQuery = true, value = "SELECT sede_id_sede FROM proyectogtics.doctor where id_doctor=?1")
    int buscarIdSedeDoctor(String idDoctor);

    Doctor findByCorreo(String correo);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE proyectogtics.doctor SET sede_id_sede = ?1 WHERE (id_doctor = ?2)")
    void actualizarSede(int idSede,String idDoctor);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`doctor` SET `foto` = null WHERE (`id_doctor` = ?1)")
    void quitarFoto(String idPaciente);

    @Query(nativeQuery = true, value = "Select * from doctor where id_doctor=?1")
    Doctor buscarHorarioPorDoctorId(String idDoctor);

}
