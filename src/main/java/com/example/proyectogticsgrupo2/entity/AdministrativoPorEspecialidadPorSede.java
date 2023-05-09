package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sede_x_especialidad_x_administrativo")
public class AdministrativoPorEspecialidadPorSede {
    @EmbeddedId
    private AdministrativoPorEspecialidadPorSedeId id;

    @MapsId("idAdministrativo")
    @ManyToOne
    @JoinColumn(name = "administrativo_id_administrativo")
    private Administrativo administrativoId;

    @MapsId("idSede")
    @ManyToOne
    @JoinColumn(name = "sede_id_sede")
    private Sede sedeId;

    @MapsId("idEspecialidad")
    @ManyToOne
    @JoinColumn(name = "especialidad_id_especialidad")
    private Especialidad especialidadId;

    @Column(name="precio_cita")
    private Float precio_cita;

    @Column(name="torre")
    private String torre;

    @Column(name = "piso")
    private String piso;

}
