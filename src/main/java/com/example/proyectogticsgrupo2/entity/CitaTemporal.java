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
    private Integer id_cita_temporal;
    private String paciente_id_paciente;
    private Integer modalidad;
    private Integer sede;
    private String doctor_id_doctor;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private Integer id_cita_previa;
}
