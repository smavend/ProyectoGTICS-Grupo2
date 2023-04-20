package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="horario")
public class Horario {

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
