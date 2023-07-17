package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.SpringSession;
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
public interface SpringSessionRepository extends JpaRepository<SpringSession, String> {

    @Query(nativeQuery = true, value = "select * from spring_session")
    List<SpringSession> buscarSesiones();
}

