package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    @Query(nativeQuery = true, value = "select * from notificacion where id_administrativo = ?1 and fecha-now()>0")
    List<Notificacion> buscarPorAdministrativoYActual(String id);

    @Query(nativeQuery = true, value = "select * from notificacion where id_paciente=?1 and revisado=0")
    List<Notificacion> buscarNotificacionesNoLeidas(String id);

    @Query(nativeQuery = true, value = "SELECT * FROM notificacion WHERE id_paciente =?1 ORDER BY id_notificacion DESC LIMIT 3")
    List<Notificacion> buscarNotificaciones(String id);



    @Query(nativeQuery = true, value = "SELECT now() - fecha FROM notificacion where fecha=?1")
    Integer fechaActual(LocalDateTime fecha);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`notificacion` SET `revisado` = '1' WHERE (`id_notificacion` =?1)")
    void SetearA1(int id_notificacion);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `proyectogtics`.`notificacion` (`id_paciente`, `titulo`, `descripcion`, `fecha`, `revisado`, `tipo_notificacion`) VALUES (?1, 'Cuestionario de cita (IMPORTANTE)', 'Dé click aquí para redirigirlo a su encuesta', now(), '0', '1')")
    void crearNotificacionDeCuestionario(String idPaciente);

    @Query(nativeQuery = true, value = "select * from notificacion where tipo_notificacion=1")
    List<Notificacion> BuscarporTipoNoti();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `proyectogtics`.`notificacion` WHERE (`id_notificacion` = ?1);")
    void eliminarNotificacionDeCuestionario(int idNotificacion);




}
