package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos, c.modalidad, c.inicio, c.fin, c.estado,c.seguro_id_seguro,p.correo \n" +
            "FROM cita c \n" +
            "INNER JOIN doctor d ON d.id_doctor = c.doctor_id_doctor \n" +
            "INNER JOIN paciente p ON p.id_paciente = c.paciente_id_paciente \n" +
            "WHERE c.doctor_id_doctor = ?1" +
            "  AND c.estado <> 4 \n" +
            "  AND c.sede_id_sede = d.sede_id_sede \n" +
            "  AND c.fin > CURRENT_TIME()\n"+
            "ORDER BY c.inicio ASC\n",
            nativeQuery = true)
        //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorProxCitas(String id);


    @Query(value = "SELECT MAX(c.id_cita), p.id_paciente, p.nombre, p.apellidos,p.foto,p.fotoname,p.fotocontenttype FROM proyectogtics.cita c INNER JOIN proyectogtics.doctor d ON (d.id_doctor=c.doctor_id_doctor) INNER JOIN proyectogtics.paciente p ON (p.id_paciente=c.paciente_id_paciente) WHERE doctor_id_doctor=?1 AND c.sede_id_sede = d.sede_id_sede  group by p.id_paciente",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorDoctorListaPacientes(String id);

    @Query(value = " select * FROM proyectogtics.cita WHERE doctor_id_doctor=?1 AND c.sede_id_sede =?2",
            nativeQuery = true) //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<Cita> listaCitasSesion(String id,int sede);


    @Query(nativeQuery = true, value = "select c.* from cita c " +
            "inner join paciente p on (c.paciente_id_paciente = p.id_paciente) " +
            "where NOW() <= c.fin and p.id_paciente = ?1 and c.estado != 4 " +
            "order by c.inicio DESC")
    List<Cita> buscarProximasCitas(String idPaciente);


    @Query(nativeQuery = true, value = "select x.torre, x.piso, x.precio_cita as precio  from cita c " +
            "inner join paciente p on (c.paciente_id_paciente = p.id_paciente) " +
            "inner join doctor d on (d.id_doctor = c.doctor_id_doctor) " +
            "inner join sede_x_especialidad_x_administrativo x on (c.sede_id_sede = x.sede_id_sede) " +
            "where NOW() <= c.fin and x.especialidad_id_especialidad = d.especialidad_id_especialidad and p.id_paciente = ?1 and c.estado != 4 " +
            "order by c.inicio DESC")
    List<TorreYPisoDTO> buscarTorresPisosPrecioProximasCitas(String idPaciente);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `link` = ?1 WHERE (`id_cita` = ?2)")
    void guardarLink(String link,int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `estado` ='3' WHERE (`id_cita` = ?1)")
    void actualizarEstadoEnConsulta(int idCita);

    /*
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `estado` =?1 WHERE (`id_cita` = ?2)")
    void actualizarEstadoEnConsulta(int estado,int idCita);

     */

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `estado` =?1 WHERE (`id_cita` = ?2)")
    void actualizarEstadoEnEspera(int nuevoEstado, Integer idCita);

    @Query(nativeQuery = true, value = "select c.* from cita c " +
            "inner join doctor d on (c.doctor_id_doctor = d.id_doctor) " +
            "inner join paciente p on (c.paciente_id_paciente = p.id_paciente) " +
            "where p.id_paciente = ?1 and NOW() >= c.fin and c.estado != 4 " +
            "order by c.inicio DESC")
    List<Cita> buscarHistorialDeCitas(String idPaciente);

    @Query(nativeQuery = true, value = "select x.torre, x.piso, x.precio_cita as precio  from cita c " +
            "inner join paciente p on (c.paciente_id_paciente = p.id_paciente) " +
            "inner join doctor d on (d.id_doctor = c.doctor_id_doctor) " +
            "inner join sede_x_especialidad_x_administrativo x on (c.sede_id_sede = x.sede_id_sede) " +
            "where p.id_paciente = ?1 and NOW() >= c.fin and c.estado = 4 and x.especialidad_id_especialidad = d.especialidad_id_especialidad " +
            "order by c.inicio DESC")
    List<TorreYPisoDTO> buscarTorresPisosPrecioHistorialCitas(String idPaciente);

    @Query(nativeQuery = true, value = "select c.* from cita c " +
            "inner join cita cp on (c.id_cita_previa = cp.id_cita) " +
            "where c.paciente_id_paciente = ?1 and date_add(cast(cp.fin as date), interval 7 day) >= now() and c.estado = 5 " +
            "order by cp.fin desc")
    List<Cita> buscarCitasPendientes(String idPaciente);

    @Query(nativeQuery = true, value = "select date_add(cast(cp.fin as date), interval 7 day) as date from cita c " +
            "inner join cita cp on (c.id_cita_previa = cp.id_cita) " +
            "where c.paciente_id_paciente = ?1 and date_add(cast(cp.fin as date), interval 7 day) >= now() and c.estado = 5 " +
            "order by cp.fin desc")
    List<Date> buscarFechasLimitesDeCitasPendientes(String idPaciente);


    @Query(nativeQuery = true, value = "select date_add(cast(cp.fin as date), interval 7 day) as date from cita c " +
            "inner join cita cp on (c.id_cita_previa = cp.id_cita) " +
            "where c.id_cita = ?1 and c.estado = 5")
    Date buscarFechaLimiteDeCitaPendiente(int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `proyectogtics`.`cita` (`paciente_id_paciente`, `doctor_id_doctor`, `inicio`, `fin`, `modalidad`, `estado`, `sede_id_sede`, `seguro_id_seguro`, `especialidad_id_especialidad`) VALUES (?1, ?2, ?3, ?4, ?5, 0, ?6, ?7, ?8)")
    void reservarCita(String idPaciente, String idDoctor, LocalDateTime inicio, LocalDateTime fin, int modalidad, int idSede, int idSeguro, int idEspecialidad);

    @Query(nativeQuery = true, value = "SELECT LAST_INSERT_ID() FROM cita")
    int obtenerUltimoId();

    @Query(nativeQuery = true, value = "select id_cita_previa from proyectogtics.cita where id_cita = ?1")
    Integer buscarIdCitaPrevia(int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `proyectogtics`.`cita` (`paciente_id_paciente`, `doctor_id_doctor`, `modalidad`, `estado`, `sede_id_sede`, `id_cita_previa`, `seguro_id_seguro`, `especialidad_id_especialidad`) " +
            "VALUES (?1, ?2, ?3, 5, ?4, ?5, ?6, ?7)")
    void guardarCitaPendiente(String idPaciente, String idDoctor, int modalidad, int idSede, int idCitaPrevia, int idSeguro, int idEspecialidad);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `doctor_id_doctor` = ?1, `inicio` = ?2, `fin` = ?3, `estado` = 5 WHERE (`id_cita` = ?4)")
    void reservarExamenPendiente(String idDoctor, LocalDateTime inicio, LocalDateTime fin, int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cita` SET `doctor_id_doctor` = ?1, `inicio` = ?2, `fin` = ?3, `modalidad` = ?4, `estado` = 1 WHERE (`id_cita` = ?5)")
    void reservarCitaPendiente(String idDoctor, LocalDateTime inicio, LocalDateTime fin, int modalidad, int idCita);

    @Query(value = "SELECT DATE_FORMAT(pag.fecha_emitida, '%d/%m/%Y') as fecha, concat(p.nombre,' ',p.apellidos) as nombres, " +
            "MAX(ROUND((sea.precio_cita*seg.doctor),2)) as pago_doctor, c.id_cita, d.id_doctor, s.nombre as sede " +
            "from pago pag " +
            "inner join cita c on (c.id_cita=pag.cita_id_cita) " +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor) " +
            "inner join paciente p on (c.paciente_id_paciente=p.id_paciente) " +
            "inner join sede s on(c.sede_id_sede=s.id_sede) " +
            "inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) " +
            "inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) " +
            "where d.id_doctor=?1 and pag.estado_pago=1 and d.especialidad_id_especialidad=sea.especialidad_id_especialidad group by pag.id_pago", nativeQuery = true )
    List<ListaRecibosDTO> listarRecibos(String id_doctor);

    @Query(value = "SELECT DATE_FORMAT(pag.fecha_emitida, '%d/%m/%Y') as fecha, concat(p.nombre,' ',p.apellidos) as nombres, MAX(ROUND((sea.precio_cita*seg.doctor),2)) as pago_doctor, c.id_cita, d.id_doctor, s.nombre as sede from pago pag inner join cita c on (c.id_cita=pag.cita_id_cita) inner join doctor d on (c.doctor_id_doctor=d.id_doctor) inner join paciente p on (c.paciente_id_paciente=p.id_paciente) inner join sede s on(c.sede_id_sede=s.id_sede) inner join sede_x_especialidad_x_administrativo sea on (s.id_sede=sea.sede_id_sede) inner join seguro seg on(seg.id_seguro=p.seguro_id_seguro) where d.id_doctor=?1 and pag.cita_id_cita=?2 and pag.estado_pago=1 and d.especialidad_id_especialidad=sea.especialidad_id_especialidad group by pag.id_pago", nativeQuery = true)
    Optional<ListaRecibosDTO> buscarRecibosPorIdCitaIdDoctor(String id_doctor, int id_cita);

    @Query(value = "SELECT c.id_cita, p.id_paciente, p.nombre, p.apellidos, c.modalidad, c.inicio, c.fin, c.estado \n" +
            "FROM proyectogtics.cita c \n" +
            "INNER JOIN proyectogtics.doctor d ON d.id_doctor = c.doctor_id_doctor \n" +
            "INNER JOIN proyectogtics.paciente p ON p.id_paciente = c.paciente_id_paciente \n" +
            "WHERE c.paciente_id_paciente = ?1 AND c.inicio >= CURRENT_TIMESTAMP \n" +
            "ORDER BY c.inicio ASC",
            nativeQuery = true)
        //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<ListaBuscadorDoctor> listarPorPacienteProxCitas(String id);

    @Query(value = "SELECT fin, tratamiento,diagnostico, receta \n" +
            "FROM proyectogtics.cita \n" +
            "WHERE paciente_id_paciente = ?1 and estado =?2",
            nativeQuery = true)
        //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    List<TratamientoDTO> listarTratamientos(String id, int estado);

    @Query(value = "Select * from cuestionario_x_cita cxc inner join cuestionario c on (cxc.cuestionario_id_cuestionario=c.id_cuestionario) where cxc.cita_id_cita=?1",
            nativeQuery = true)
        //TENER CUIDADO CON El PUNTO Y COMA AL FINAL DEL QUERY PQ SINO, NO FUNCIONA
    Optional<CuestionarioxDoctorDTO> verCuestionario(int id);

    @Query(value = "SELECT cita.id_cita, cita.fin\n" +
            "FROM proyectogtics.cita\n" +
            "INNER JOIN proyectogtics.cuestionario_x_cita ON cita.id_cita = cuestionario_x_cita.cita_id_cita\n" +
            "WHERE cita.paciente_id_paciente = ?1" +
            "    AND cita.estado = ?2", nativeQuery = true)
    List<EncuestaDoctorDTO> listarFechaEncuesta(String idPaciente, int estado);

    @Query(value = "SELECT * FROM proyectogtics.cita\n" +
            "WHERE  paciente_id_paciente= ?1" +
            "  AND especialidad_id_especialidad IN (4, 5, 6)",nativeQuery = true)
    List<Cita> listarExamenes(String idPaciente);

    @Query(value = "select * from cita where doctor_id_doctor=?1 and sede_id_sede=?2",nativeQuery = true)
    List<Cita> obtenerCitasPorDoctorId(String idDoctor,int idSede);

    @Query(value = "select * from cita where sede_id_sede=?1",nativeQuery = true)
    List<Cita> obtenerTodasLasCitas(Integer idSede);

    @Query(nativeQuery = true, value = "select * from cita c where c.id_cita = ?1")
    Cita buscarPorId(Integer idCita); // tuve que crearla porque no me buscaba las citas pendientes xd
}
