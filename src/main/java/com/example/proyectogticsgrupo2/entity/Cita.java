package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalDateTime inicio;
    @Column(nullable = false)
    private LocalDateTime fin;

    @NotBlank(message = "El campo no puede estar vacío", groups = validacion.class)
    @Size(max=500,message = "El campo no puede tener más de 500 caracteres")
    private String diagnostico;

    @NotBlank(message = "El campo no puede estar vacío", groups = validacion.class)
    @Size(max=500,message = "El campo no puede tener más de 500 caracteres")
    private String receta;

    @NotBlank(message = "El campo no puede estar vacío", groups = validacion.class)
    @Size(max=500,message = "El campo no puede tener más de 500 caracteres")
    private String tratamiento;

    @Size(max=500,message = "El campo no puede tener más de 500 caracteres")
    private String bitacora;
    @Column(nullable = false)
    private int modalidad;

    /* modalidad == 0 ---> Presencial
       modalidad == 1 ---> Virtual
       modalidad == 2 ---> Examen*/

    @Column(nullable = false)
    @Lob
    private byte[] reporte;


    @Column(nullable = false)
    private int estado;

    /* estado == 0 ---> Pendiente de pago
       estado == 1 o 2 ---> En espera
       estado == 3 ---> En consulta
       estado == 4 ---> Consulta finalizada
       estado == 5 ---> Examen pendiente */

    @ManyToOne
    @JoinColumn(name = "sede_id_sede", nullable = false)
    private Sede sede;

    @ManyToOne
    @JoinColumn(name = "id_cita_previa")
    private Cita cita_previa;

    @Column(name = "seguro_id_seguro")
    private String idSeguro;

    @ManyToOne
    @JoinColumn(name = "especialidad_id_especialidad")
    private Especialidad especialidad;

    @Lob
    private byte[] examendoc;
    private String examenname;
    private String examencontenttype;

    private String link;

    public interface validacion{}

}
