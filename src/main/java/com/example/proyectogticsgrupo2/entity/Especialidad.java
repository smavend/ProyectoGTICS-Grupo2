package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="especialidad")
public class Especialidad {
    @Id
    private String id_especialidad;
    private String nombre;
}
