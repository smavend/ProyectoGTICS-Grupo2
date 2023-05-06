package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sede")
public class Sede {

    @Id
    @Column(name = "id_sede", nullable = false)
    private int idSede;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name="clinica_id_clinica", nullable = false)
    private Clinica clinica;

    @Column
    private String direccion;

    private String fotoname;
    private String fotocontenttype;
    private byte[] foto;

}
