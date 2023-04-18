package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="sede")
@Getter
@Setter
public class Sede {
    @Id
    @Column(name = "id_sede", nullable = false)
    private String id_sede;
    @Column(nullable = false)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="clinica_id_clinica",nullable = false)
    private Clinica clinica_id_clinica;

    @OneToMany(mappedBy = "sede")
    private List<SedeXEspecialidadXAdministrativo> sedesAdministrativo;
}
