package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede,Integer> {
}
