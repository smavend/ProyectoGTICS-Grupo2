package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "administrativo")
public class Administrativo {

    @Id
    @Column(name = "id_administrativo", nullable = false)
    private String idAdministrativo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private int estado; //no activo: 0,activo: 1,

    @Column(nullable = false)
    private String correo;

    @OneToMany(mappedBy = "administrativo")
    private List<SedeXEspecialidadXAdministrativo> sedesAdministrativo;

    @OneToMany(mappedBy = "administrativo")
    private List<Paciente> paciente_administrativo;
}
