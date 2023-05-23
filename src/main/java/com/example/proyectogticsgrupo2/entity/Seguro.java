package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "seguro")
public class Seguro implements Serializable {
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
