package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sede")
public class Sede implements Serializable {

    @Id
    @Column(name = "id_sede", nullable = false)
    @Min(value = 1, message = "error")
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
