package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class PacientePorConsentimientoID implements Serializable {
    @Column(name = "paciente_id_paciente", nullable = false)
    private String idPaciente;
    @Column(name = "consentimientos_id_consentimiento", nullable = false)
    private Integer idConsentimiento;
}
