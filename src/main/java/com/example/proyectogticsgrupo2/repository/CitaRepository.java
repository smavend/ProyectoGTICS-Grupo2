package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.ListaBuscadorDoctor;
import com.example.proyectogticsgrupo2.dto.ListaRecibosDTO;
import com.example.proyectogticsgrupo2.dto.TratamientoDTO;
import com.example.proyectogticsgrupo2.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor=?1",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorProxCitas(String id);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor=?1 group by id_paciente",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorListaPacientes(String id);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor= ?1 and lower(concat(p.nombre,' ',p.apellidos)) like %?2%", nativeQuery = true)
    List<ListaBuscadorDoctor> buscadorProximasCitas(String id,String nombre);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos,c.modalidad,c.inicio,c.fin,c.estado FROM cita c inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join paciente p on (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor= ?1 and lower(concat(p.nombre,' ',p.apellidos)) like %?2% group by id_paciente", nativeQuery = true)
    List<ListaBuscadorDoctor> buscadorPaciente(String id,String nombre);

    @Query(value = "SELECT DATE_FORMAT(c.inicio, '%d/%m/%Y') as fecha , concat(p.nombre,' ',p.apellidos) as nombres, ROUND((sea.precio_cita*seg.doctor),2) as pago_doctor, c.id_cita, p.id_paciente,d.id_doctor, seg.doctor   from cita c inner join sede s on (s.id_sede=c.sede_id_sede) inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) inner join paciente p on(p.id_paciente=c.paciente_id_paciente) inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) where c.doctor_id_doctor=?1 order by DATE_FORMAT(c.inicio, '%d/%m/%Y')", nativeQuery = true)
    List<ListaRecibosDTO> listarRecibos(String id_doctor);

    @Query(value = "SELECT DATE_FORMAT(c.inicio, '%d/%m/%Y') as fecha , concat(p.nombre,' ',p.apellidos) as nombres, ROUND((sea.precio_cita*seg.doctor),2) as pago_doctor, c.id_cita, p.id_paciente,d.id_doctor, seg.doctor   from cita c inner join sede s on (s.id_sede=c.sede_id_sede) inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) inner join paciente p on(p.id_paciente=c.paciente_id_paciente) inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) where c.doctor_id_doctor=?1 and (lower(concat(p.nombre,' ',p.apellidos)) LIKE CONCAT('%',LOWER(?2),'%')) order by DATE_FORMAT(c.inicio, '%d/%m/%Y')", nativeQuery = true)
    List<ListaRecibosDTO> buscarRecibosNombre(String id_doctor,String searchfield);

    @Query(value = "SELECT DATE_FORMAT(c.inicio, '%d/%m/%Y') as fecha , concat(p.nombre,' ',p.apellidos) as nombres, ROUND((sea.precio_cita*seg.doctor),2) as pago_doctor, c.id_cita, p.id_paciente,d.id_doctor, seg.doctor   from cita c inner join sede s on (s.id_sede=c.sede_id_sede) inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) inner join paciente p on(p.id_paciente=c.paciente_id_paciente) inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) where c.doctor_id_doctor=?1 and  (sea.precio_cita*seg.doctor) LIKE %?2% order by DATE_FORMAT(c.inicio, '%d/%m/%Y')", nativeQuery = true)
    List<ListaRecibosDTO> buscarRecibosPago(String id_doctor,String searchfield);

    @Query(value = "SELECT DATE_FORMAT(c.inicio, '%d/%m/%Y') as fecha , concat(p.nombre,' ',p.apellidos) as nombres, ROUND((sea.precio_cita*seg.doctor),2) as pago_doctor, c.id_cita, p.id_paciente,d.id_doctor, seg.doctor   from cita c inner join sede s on (s.id_sede=c.sede_id_sede) inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) inner join paciente p on(p.id_paciente=c.paciente_id_paciente) inner join doctor d on (d.id_doctor=c.doctor_id_doctor) inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) where c.doctor_id_doctor=?1 and c.id_cita=?2 order by DATE_FORMAT(c.inicio, '%d/%m/%Y')", nativeQuery = true)
    Optional<ListaRecibosDTO> buscarRecibosPorIdCitaIdDoctor(String id_doctor, int id_cita);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos, c.modalidad, c.inicio, c.fin, c.estado\n" +
            "FROM proyectogtics.cita c\n" +
            "INNER JOIN proyectogtics.doctor d ON d.id_doctor = c.doctor_id_doctor\n" +
            "INNER JOIN proyectogtics.paciente p ON p.id_paciente = c.paciente_id_paciente\n" +
            "WHERE c.paciente_id_paciente = ?1 AND c.inicio >= CURRENT_TIMESTAMP\n" +
            "GROUP BY c.id_cita, p.id_paciente, p.nombre, p.apellidos, c.modalidad, c.inicio, c.fin, c.estado\n" +
            "ORDER BY c.inicio ASC\n",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorPacienteProxCitas(String id);
    @Query(value = "SELECT diagnostico, receta, tratamiento\n" +
            "            FROM proyectogtics.cita \n" +
            "            WHERE paciente_id_paciente = ?1;",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<TratamientoDTO> listarTratamientos(String id);


}
