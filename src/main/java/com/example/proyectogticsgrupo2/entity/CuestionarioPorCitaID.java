package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class CuestionarioPorCitaID implements Serializable {
    @Column(name = "cuestionario_id_cuestionario", nullable = false)
    private Integer idCuestionario;
    @Column(name = "cita_id_cita", nullable = false)
    private Integer idCita;
}
