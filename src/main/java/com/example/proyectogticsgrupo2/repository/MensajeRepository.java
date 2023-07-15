package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje,Integer> {
    //List<Mensaje> findById_receptor(String destinatario);
}
