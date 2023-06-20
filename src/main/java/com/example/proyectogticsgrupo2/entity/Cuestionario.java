package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cuestionario")
public class Cuestionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cuestionario;
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private String p5;
    private String p6;
    private String p7;
    private String p8;
    private String p9;
    private String p10;
    private String p11;
}
