package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @Column(name = "id_especialidad", nullable = false)
    private Integer idEspecialidad;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "especialidad")
    private List<SedeXEspecialidadXAdministrativo> sedesAdministrativo;
}
