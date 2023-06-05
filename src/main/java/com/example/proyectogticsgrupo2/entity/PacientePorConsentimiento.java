package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "paciente_has_consentimientos")
public class PacientePorConsentimiento {
    @EmbeddedId
    private PacientePorConsentimientoID id;

    @MapsId(value = "idPaciente")
    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente")
    private Paciente paciente;

    @MapsId(value = "idConsentimiento")
    @ManyToOne
    @JoinColumn(name = "consentimientos_id_consentimiento")
    private Consentimiento consentimiento;

    private Integer valor;
}
