package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AlergiaXPacienteId implements Serializable {
    private static final long serialVersionUID = 4647823628345253333L;

    @Column(name = "alergias_id_alergia", nullable = false)
    private Integer alergiasIdAlergia;

    @Size(max = 8)
    @Column(name = "paciente_id_paciente", nullable = false, length = 8)
    private String pacienteIdPaciente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AlergiaXPacienteId entity = (AlergiaXPacienteId) o;
        return Objects.equals(this.pacienteIdPaciente, entity.pacienteIdPaciente) &&
                Objects.equals(this.alergiasIdAlergia, entity.alergiasIdAlergia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pacienteIdPaciente, alergiasIdAlergia);
    }

}