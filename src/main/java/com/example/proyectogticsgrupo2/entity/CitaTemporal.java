package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cita_temporal")
public class CitaTemporal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_temporal")
    private Integer id;

    @Column(name = "paciente_id_paciente")
    private String idPaciente;

    private Integer modalidad;

    @Column(name = "sede_id_sede")
    private Integer idSede;

    @Column(name = "doctor_id_doctor")
    private String idDoctor;

    private LocalDateTime inicio;
    private LocalDateTime fin;

    @Column(name = "id_cita_previa")
    private Integer idCitaPrevia;

    @Column(name = "id_especialidad")
    private Integer idEspecialidad;
}
