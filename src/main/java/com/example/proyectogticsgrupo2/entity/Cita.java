package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="cita")
public class Cita {
    @Id
    @Column(name = "id_cita", nullable = false)
    private String id_cita;

    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "doctor_id_doctor", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private String inicio;
    @Column(nullable = false)
    private String fin;
    @Column(nullable = false)
    private String receta;
    private String bitacora;
    @Column(nullable = false)
    private String modalidad;
    @Column(nullable = false)
    private String reporte;
    private String link_cita;
    private String direccion;
    @Column(nullable = false)
    private String estado;
    private String fecha_emision;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede", nullable = false)
    private Sede sede;

    private String es_examen;

    @ManyToOne
    @JoinColumn(name = "id_cita_previa")
    private Cita cita;


}
