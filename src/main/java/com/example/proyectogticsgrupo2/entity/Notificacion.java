package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@Entity
@Table(name="notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion", nullable = false)
    private int id_notificacion;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_administrador", nullable = false)
    private Administrador administrador;

    @ManyToOne
    @JoinColumn(name = "id_administrativo", nullable = false)
    private Administrativo administrativo;

    @Column(name = "titulo",nullable = false)
    private String titulo;

    @Column(name = "descripcion",nullable = false)
    private String descripcion;

    @Column(name = "fecha",nullable = false)
    private LocalDateTime fecha;

    @Column(name = "revisado",nullable = false)
    private int revisado; //0: no revisado, 1: revisado

    @Column(name = "tipo_notificacion")
    private int tipo_notificacion;
}
