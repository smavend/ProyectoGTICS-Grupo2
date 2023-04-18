package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="sede")
public class Sede {
    @Id
    private String id_sede;
    private String nombre;
    @ManyToOne
    @JoinColumn(name="clinica_id_clinica")
    private Clinica clinica;
}
