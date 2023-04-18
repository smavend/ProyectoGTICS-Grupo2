package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="clinica")
@Getter
@Setter
public class Clinica {
    @Id
    @Column(name="id_clinica", nullable = false)
    private Integer idClinica;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private String tyc;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String correo;
}
