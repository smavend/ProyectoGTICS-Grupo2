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
    private String id_cita;

    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "doctor_id_doctor")
    private Doctor doctor;

    private String inicio;
    private String fin;
    private String receta;
    private String bitacora;
    private String modalidad;
    private String reporte;
    private String link_cita;
    private String direccion;
    private String estado;
    private String fecha_emision;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede")
    private Sede sede;

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "id_cita_previa")
    private Cita cita;


}
