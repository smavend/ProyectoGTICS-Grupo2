package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje,Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM mensaje where id_emisor=?1")
    List<Mensaje> mensajes (String id);

}
