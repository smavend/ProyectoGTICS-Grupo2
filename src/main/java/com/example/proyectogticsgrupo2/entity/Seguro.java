package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "seguro")
public class Seguro {
    @Id
    @Column(name = "id_seguro", nullable = false)
    private int idSeguro;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float coaseguro;
    @Column(nullable = false)
    private float doctor;
}
