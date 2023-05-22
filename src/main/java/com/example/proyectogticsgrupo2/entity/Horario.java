package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="horario")
public class Horario implements Serializable {

    @Id
    @Column(name = "id_horario", nullable = false)
    private String id_horario;
    @Column(nullable = false)
    private String comida_inicio;
    @Column(nullable = false)
    private String comida_fin;
    @Column(nullable = false)
    private String disponibilidad_inicio;
    @Column(nullable = false)
    private String disponibilidad_fin;

}
