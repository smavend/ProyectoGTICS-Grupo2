package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "administrador")
public class Administrador {
    @Id
    @Column(name="id_administrador", nullable = false)
    private String idAdministrador;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private String correo ;
    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0, otros

}
