package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.CuestionarioPorCita;
import com.example.proyectogticsgrupo2.entity.CuestionarioPorCitaID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuestionarioPorCitaRepository extends JpaRepository<CuestionarioPorCita, CuestionarioPorCitaID> {

    @Query(nativeQuery = true, value = "select x.* from cuestionario_x_cita x " +
            "inner join cita c on (x.cita_id_cita = c.id_cita) " +
            "inner join paciente p on (p.id_paciente = c.paciente_id_paciente) " +
            "WHERE p.id_paciente = ?1 " +
            "order by c.inicio DESC")
    List<CuestionarioPorCita> buscarPorPaciente(String idPaciente);

    @Query(nativeQuery = true, value = "SELECT CURRENT_TIMESTAMP")
    LocalDateTime FechaHora();
    CuestionarioPorCita findByIdIdCuestionarioAndIdIdCita(int idCuestionario, int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cuestionario_x_cita` " +
            "SET r1 = ?1, r2 = ?2, r3 = ?3, r4 = ?4, r5 = ?5, r6 = ?6, r7 = ?7, r8 = ?8, r9 = ?9, r10 = ?10, r11 = ?11, estado = 1, fecha_completado = NOW() " +
            "WHERE (`cuestionario_id_cuestionario` = ?12) AND (`cita_id_cita` = ?13)")
    void guardarCuestionario(String r1, String r2, String r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String r11,
                             Integer idCuestionario, Integer idCita);

    Optional<CuestionarioPorCita> findByIdIdCita(int idCita);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`cuestionario_x_cita` SET `opcion_inicio_sesion` = '1' WHERE (`cuestionario_id_cuestionario` = ?1) and (`cita_id_cita` = ?2)")
    void actualizarOpcionSesion(int idCuestionario, int idCita);
}
