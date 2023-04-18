package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sede_x_especialidad_x_administrativo")
public class SedeXEspecialidadXAdministrativo {

    @EmbeddedId
    private SedeEspecialidadAdministrativoId id;

    @ManyToOne
    @MapsId("sede_id_sede")
    @JoinColumn(name = "sede_id_sede")
    private Sede sede;

    @ManyToOne
    @MapsId("especialidad_id_especialidad")
    @JoinColumn(name = "especialidad_id_especialidad")
    private Especialidad especialidad;

    @ManyToOne
    @MapsId("administrativo_id_administrativo")
    @JoinColumn(name = "administrativo_id_administrativo")
    private Administrativo administrativo;

    public Sede getSede() {
        return this.sede;
    }

}