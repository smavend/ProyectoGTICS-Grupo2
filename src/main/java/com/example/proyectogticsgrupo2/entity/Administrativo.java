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
@Table(name="administrativo")
public class Administrativo {
    @Id
    @Column(name = "id_administrativo", nullable = false)
    private String idAdministrativo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0

    @Column(nullable = false)
    private String correo;

}
