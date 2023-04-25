package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "seguro")
public class Seguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguro", nullable = false)
    private int idSeguro;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float coaseguro;
    @Column(nullable = false)
    private float doctor;
}
