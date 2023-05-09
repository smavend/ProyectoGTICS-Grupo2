package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table (name = "temporal")
public class Temporal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_temporal")
    private int id_temporal;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar el DNI")
    @Digits(integer = 8, fraction = 0, message = "El DNI debe ser un número")
    @Size(max=8,message = "El DNI debe tener solo 8 dígitos")
    private String dni;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar un nombre")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar los apellidos")
    private String apellidos;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar un correo")
    @Email(message = "debe ingresar un correo válido")
    private String correo;

    private String telefono;

    private LocalDate fecha_nacimiento;

    private LocalDateTime fechainvitado;

    private int llenado;

    @ManyToOne
    @JoinColumn(name = "administrativo_id_administrativo")
    private Administrativo administrativo;
    @ManyToOne
    @JoinColumn(name = "seguro_id_seguro")
    private Seguro seguro;
    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito")
    private Distrito distrito;


}
