package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "especialidad")
public class Especialidad implements Serializable {

    @Id
    @Column(name = "id_especialidad", nullable = false)
    private int idEspecialidad;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int es_examen;

}
