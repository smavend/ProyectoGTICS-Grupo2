package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="clinica")
public class Clinica {

    @Id
    private String id_clinica;
    private String nombre;
    private String color;
    private String tyc;
    private String telefono;
    private String correo;

}
