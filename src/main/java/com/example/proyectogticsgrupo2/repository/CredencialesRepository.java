package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Credenciales;
import com.example.proyectogticsgrupo2.entity.Cuestionario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales,String> {
    Optional<Credenciales> findByCorreoAndContrasena(String correo, String passw);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `proyectogtics`.`credenciales` (`id_credenciales`, `correo`, `contrasena_hasheada`) VALUES (?1,?2,?3);\n")
    void crearCredenciales(String id, String correo, String password);


    @Query(nativeQuery = true, value = "SELECT * FROM credenciales WHERE id_credenciales = ?1")
    Credenciales buscarPorId(String idPaciente);
}
