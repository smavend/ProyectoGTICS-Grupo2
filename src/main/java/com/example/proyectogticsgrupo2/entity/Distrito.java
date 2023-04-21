package com.example.proyectogticsgrupo2.entity;

<<<<<<< HEAD

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "distrito")
@Getter
@Setter

public class Distrito {
    @Id
    @Column(name = "id_distrito",nullable = false)
    private Integer id_distrito;
    @Column(name="nombre")
    private String nombre_distrito;
=======
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "distrito")
public class Distrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrito", nullable = false)
    private int idDistrito;
    @Column(nullable = false)
    private String nombre;
>>>>>>> 3c6243c3ca5f8aa7c1e8747c1db386c7eaef4415
}
