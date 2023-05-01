package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "alergia_x_paciente")
public class AlergiaPorPaciente {
    @EmbeddedId
    private AlergiaPorPacienteId id;

    @MapsId(value = "id_alergia")
    @ManyToOne
    @JoinColumn(name = "alergias_id_alergia")
    private Alergia alergia;

    @MapsId(value = "id_paciente")
    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente")
    private Paciente paciente;
}
