package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SedeEspecialidadAdministrativoId implements Serializable {
    private int sede_id_sede;
    private int especialidad_id_especialidad;
    private String administrativo_id_administrativo;
}