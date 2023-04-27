package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro,Integer> {
}
