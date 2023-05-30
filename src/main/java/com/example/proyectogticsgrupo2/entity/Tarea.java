package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "tarea")
public class Tarea implements Serializable{
    @Id
    @Column(name = "id_tarea")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarea;

    private int estado;

    private int tipo; // 1: solicitan crear primera contraseña

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente")
    private Paciente paciente;
}
