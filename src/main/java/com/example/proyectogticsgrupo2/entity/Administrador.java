package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name = "administrador")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_administrador;
    private String nombre;
    private String apellidos;
    private int estado;
    private String correo;

    @Column(name = "sede_id_sede")
    private int id_sede;

}
