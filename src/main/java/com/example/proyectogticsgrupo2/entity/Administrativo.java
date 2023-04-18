package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="administrativo")
@Getter
@Setter
public class Administrativo {
    @Id
    @Column(name = "id_administrativo", nullable = false)
    private String idAdministrativo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0

    @Column(nullable = false)
    private String correo;

    @OneToMany(mappedBy = "administrativo")
    private List<SedeXEspecialidadXAdministrativo> sedesAdministrativo;

    @OneToMany(mappedBy = "administrativo")
    private List<Paciente> paciente_administrativo;
}
