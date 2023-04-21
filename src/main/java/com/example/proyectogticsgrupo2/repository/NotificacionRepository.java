package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    @Query(nativeQuery = true, value = "select * from notificacion where id_usuario = ?1 and fecha-now()>0")
    List<Notificacion> buscarPorUsuarioYActual(String id);
}
