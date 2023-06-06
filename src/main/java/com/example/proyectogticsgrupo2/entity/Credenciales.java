package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "credenciales")
public class Credenciales {
    @Id
    private String id_credenciales;

    private String correo;

    @Column(name = "contrasena_hasheada")
    private String contrasena;

    public Credenciales(String id_credenciales, String correo, String contrasena){
        this.id_credenciales = id_credenciales;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Credenciales(){

    }
}
