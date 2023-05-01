package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AlergiaPorPacienteId implements Serializable {
    @Column(name = "alergias_id_alergia", nullable = false)
    private Integer id_alergia;
    @Column(name = "paciente_id_paciente", nullable = false)
    private String id_paciente;
}
