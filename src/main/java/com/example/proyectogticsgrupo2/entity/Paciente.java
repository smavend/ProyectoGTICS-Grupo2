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

    @Column(name = "estado")
    private int estado; //no activo: 0, activo: 1,invitado: 2, registrado:3, agendado: 4, en consulta: 5

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
    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito", nullable = false)
    private Distrito distrito;

    @Column(nullable = false)
    private String direccion;

}
