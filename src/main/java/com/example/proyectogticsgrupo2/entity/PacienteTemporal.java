package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "temporal")
public class PacienteTemporal {
    @Id
    @Column(name = "id_usuario_temporal", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTemporal;

    @NotBlank(message = "Ingrese los nombres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Ingrese los apellidos")
    @Column(nullable = false)
    private String apellidos;

    @NotBlank(message = "Debe llenar el campo de correo")
    @Email(message = "Debe ingresar un correo válido")
    @Column(nullable = false)
    private String correo;

    @Column(name = "fechainvitado",nullable = false)
    private Date fechaInvitado;

    @ManyToOne
    @JoinColumn(name = "administrativo_id_administrativo", nullable = false)
    private Administrativo administrativo;
}
