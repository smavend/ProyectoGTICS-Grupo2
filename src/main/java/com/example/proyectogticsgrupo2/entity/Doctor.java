package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="doctor")
public class Doctor {
    @Id
    @Column(name = "id_doctor", nullable = false)
    private String id_doctor;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private int estado;

    @ManyToOne
    @JoinColumn(name = "especialidad_id_especialidad",nullable = false)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede",nullable = false)
    private Sede sede;

    @Column(nullable = false)
    private float duracion_cita_horas;

    @ManyToOne
    @JoinColumn(name = "horario_id_horario",nullable = false)
    private Horario horario;

    @Column(nullable = false)
    private String correo;

    private String precio;
    @Lob
    private byte[] foto;

    private String fotoname;
    private String fotocontenttype;
    private String genero;




}
