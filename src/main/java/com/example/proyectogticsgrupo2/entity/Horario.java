package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name="horario")
public class Horario implements Serializable {

    @Id
    @Column(name = "id_horario", nullable = false)
    private int id_horario;
    private LocalTime comida_inicio;
    private LocalTime disponibilidad_inicio;
    private LocalTime disponibilidad_fin;

}
