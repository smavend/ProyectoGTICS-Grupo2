package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name="horario")
public class Horario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario", nullable = false)
    private Integer id_horario;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime comida_inicio;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime disponibilidad_inicio;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime disponibilidad_fin;

}
