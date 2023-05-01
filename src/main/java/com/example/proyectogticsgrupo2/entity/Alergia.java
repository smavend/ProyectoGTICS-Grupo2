package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "alergias")
public class Alergia {
    @Id
    @Column(name = "id_alergia", nullable = false)
    private int idAlergia;

    @Column(nullable = false)
    private String nombre;
}
