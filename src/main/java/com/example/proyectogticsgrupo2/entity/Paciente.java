package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(name="id_paciente", nullable = false)
    private String idPaciente;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0, otros

    @ManyToOne
    @JoinColumn(name="seguro_id_seguro",nullable = false)
    private Seguro seguro;

    @Column(nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name="administrativo_id_administrativo")
    private Administrativo administrativo;

    @Column(nullable = false)
    private String correo;

    @Lob
    @Column(nullable = false)
    private byte[] foto;

}
