package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales,String> {
    Optional<Credenciales> findByCorreoAndContrasena(String correo, String passw);
}
