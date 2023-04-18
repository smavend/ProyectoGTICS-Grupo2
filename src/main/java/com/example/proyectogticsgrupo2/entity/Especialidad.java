package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "especialidad")
@Getter
@Setter

public class Especialidad {
    @Id
    @Column(name ="id_especialidad",nullable = false)
    private Integer id_especialidad;
    @Column(name="nombre",nullable = false)
    private String nombre;
    @OneToMany(mappedBy = "especialidad")
    private List<SedeXEspecialidadXAdministrativo> sedesAdministrativo;
}
