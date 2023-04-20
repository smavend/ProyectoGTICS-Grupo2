package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sede")
public class Sede {
    @Id
    @Column(name="id_sede", nullable = false)
    private int idSede;
    @Column(nullable = false)
    private String nombre;
    @OneToOne
    @JoinColumn(name = "clinica_id_clinica", nullable = false)
    private Clinica clinica;
}
