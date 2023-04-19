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
    private String id_doctor;
    private String nombre;
    private String apellidos;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "especialidad_id_especialidad")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede")
    private Sede sede;

    private String duracion_cita_horas;

    @ManyToOne
    @JoinColumn(name = "horario_id_horario")
    private Horario horario;

    private String correo;
    private String foto;
    private String precio;


}
