package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByIdPacienteAndToken(String idPaciente, String token);
}
