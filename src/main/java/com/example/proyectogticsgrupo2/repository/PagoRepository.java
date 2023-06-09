package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.PagoYPrecioDTO;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `proyectogtics`.`pago` SET `fecha_cancelada` = NOW(), `estado_pago` = '1' WHERE (`id_pago` = ?)")
    void guardarPago(int idPago);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO pago (`fecha_emitida`, `estado_pago`, `cita_id_cita`) VALUES (NOW(), 0, ?1)")
    void nuevoPago(int idCita);
}
