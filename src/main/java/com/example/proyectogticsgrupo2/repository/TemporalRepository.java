package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.TemporalDiasDto;
import com.example.proyectogticsgrupo2.entity.Temporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TemporalRepository extends JpaRepository<Temporal, Integer> {
    @Query(nativeQuery = true, value = "SELECT t.*, datediff(now(), fechainvitado) as dias FROM temporal t")
    List<TemporalDiasDto> obtenerDias();
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE temporal SET nombre = ?1, apellidos = ?2, correo = ?3 WHERE (id_usuario_temporal = ?4)")
    void actualizarInvitado(String nombre, String apellidos, String correo, int idTemporal);

    public List<Temporal> findByAdministrativo_IdAdministrativo(String idAdministrativo);
}
