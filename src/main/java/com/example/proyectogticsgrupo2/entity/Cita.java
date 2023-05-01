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
    private int id_cita;

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
    private String diagnostico;
    private String receta;
    private String bitacora;
    @Column(nullable = false)
    private int modalidad;
    @Column(nullable = false)

    @Lob
    private byte[] reporte;

    private String link_cita;
    private String direccion;
    @Column(nullable = false)
    private int estado;
    private String fecha_emision;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede", nullable = false)
    private Sede sede;

    @ManyToOne
    @JoinColumn(name = "id_cita_previa")
    private Cita cita_previa;


}
