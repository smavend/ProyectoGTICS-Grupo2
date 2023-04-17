package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Seguro {
    @Id
    @Column(name = "id_seguro", nullable = false)
    private String idSeguro;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float coaseguro;
    @Column(nullable = false)
    private float doctor;
}
