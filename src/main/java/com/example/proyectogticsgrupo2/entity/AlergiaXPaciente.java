package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "alergia_x_paciente")
public class AlergiaXPaciente {
    @EmbeddedId
    private AlergiaXPacienteId id;

    @MapsId("alergiasIdAlergia")
    @ManyToOne(optional = false)
    @JoinColumn(name = "alergias_id_alergia", nullable = false)
    private Alergia alergiasIdAlergia;

    @MapsId("pacienteIdPaciente")
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id_paciente", nullable = false)
    private Paciente pacienteIdPaciente;

}