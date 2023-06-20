package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Cuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM cuestionario WHERE id_cuestionario = ?1")
    Cuestionario buscarPorId(Integer idCuestionario);
}
