package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "token")
public class Token {
    @Id
    @Column(name = "paciente_id_paciente", nullable = false)
    private String idPaciente;

    private String token;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    private int tabla;
    /*
    1: temporal
    2: paciente
    */
}
