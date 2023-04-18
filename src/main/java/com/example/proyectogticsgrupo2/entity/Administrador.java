package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "administrador")
@Getter
@Setter
public class Administrador {
    @Id
    @Column(name = "id_administrador", nullable = false)
    private String idAdmin;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0, otros
    @ManyToOne
    @JoinColumn(name="sede_id_sede",nullable = false)
    private Sede sede_id ;
    @Column(nullable = false)
    private String correo;

}
