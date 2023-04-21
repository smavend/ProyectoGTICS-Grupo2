package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Administrativo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativoRepository extends JpaRepository<Administrativo, String> {
}
