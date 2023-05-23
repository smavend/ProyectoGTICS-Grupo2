package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
    SuperAdmin findByCorreo(String correo);
}
