package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.ListaRecibosDTO;
import com.example.proyectogticsgrupo2.dto.PagoYPrecioDTO;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`pago` SET `fecha_cancelada` = NOW(), `estado_pago` = '1' WHERE (`id_pago` = ?)")
    void guardarPago(int idPago);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO pago (`fecha_emitida`, `estado_pago`, `cita_id_cita`, `tipo_pago`) VALUES (NOW(), 0, ?1, ?2)")
    void nuevoPago(int idCita, String tipoPago);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO pago (`fecha_emitida`, `estado_pago`, `cita_id_cita`) VALUES (NOW(), 0, ?1)")
    void nuevoPagoDeSoloExamen(int idCita);

    @Query(value = "Select * from pago where cita_id_cita=?1", nativeQuery = true )
    Pago buscarPagoPorIdcita(int idCita);


    @Query(nativeQuery = true, value = "select p.*, c.inicio from pago p " +
            "inner join cita c on (p.cita_id_cita = c.id_cita) " +
            "where c.paciente_id_paciente = ?1 " +
            "order by c.inicio DESC")
    List<Pago> buscarPorPaciente(String idPaciente);
}
