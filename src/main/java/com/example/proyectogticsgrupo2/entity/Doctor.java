package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name="doctor")
public class Doctor {
    @Id
    @Column(name = "id_doctor", nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Digits(integer = 8, fraction = 0, message = "En DNI debe ser un número")
    @Size(min = 8, max = 8, message = "En DNI debe tener 8 dígitos" )
    private String id_doctor;
    @Column(nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    private String nombre;
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El apellido no puede tener más de 45 caracteres")
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

    @Column(name = "duracion_cita_horas")
    private String duracion_cita_horas;

    @Column(name = "pregrado")
    private String pregrado;

    @Column(name = "posgrado")
    private String posgrado;

    @Column(name = "colegiatura")
    private String colegiatura;
    @ManyToOne
    @JoinColumn(name = "horario_id_horario")
    private Horario horario;

    @Column(nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Email(message = "Ingrese una dirección de correo válida")
    private String correo;

    private String fotoname;
    private String fotocontenttype;
    private byte[] foto;

    @NotBlank(message = "Seleccione un género")
    private String genero;


}
