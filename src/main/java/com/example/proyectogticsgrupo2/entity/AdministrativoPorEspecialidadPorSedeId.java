package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
/*
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
 */
public class AdministrativoPorEspecialidadPorSedeId implements Serializable {
    @Column(name = "sede_id_sede", nullable = false)
    private int idSede;
    @Column(name = "especialidad_id_especialidad", nullable = false)
    private int idEspecialidad;
    @Column(name = "administrativo_id_administrativo", nullable = false)
    private String idAdministrativo;
}
