package com.example.proyectogticsgrupo2.entity;


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
}
