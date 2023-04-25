package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
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
    private int idTemporal;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String correo;

    @Column(name = "fechainvitado",nullable = false)
    private Date fechaInvitado;

    @ManyToOne
    @JoinColumn(name = "administrativo_id_administrativo", nullable = false)
    private Administrativo administrativo;
}
