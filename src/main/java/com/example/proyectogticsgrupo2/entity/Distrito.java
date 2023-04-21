package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "distrito")
@Getter
@Setter

public class Distrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrito", nullable = false)
    private Integer idDistrito;
    @Column(name = "nombre")
    private String nombre;
}
