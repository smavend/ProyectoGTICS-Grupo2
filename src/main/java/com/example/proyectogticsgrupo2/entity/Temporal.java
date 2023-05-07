package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
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
    private String dni;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
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
