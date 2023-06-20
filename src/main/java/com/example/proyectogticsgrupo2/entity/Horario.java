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
    @Column(name = "comida_inicio_lunes", nullable = false)
    private LocalTime comida_inicio_lunes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_lunes", nullable = false)
    private LocalTime disponibilidad_inicio_lunes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_lunes", nullable = false)
    private LocalTime disponibilidad_fin_lunes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "comida_inicio_martes", nullable = false)
    private LocalTime comida_inicio_martes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_martes", nullable = false)
    private LocalTime disponibilidad_inicio_martes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_martes", nullable = false)
    private LocalTime disponibilidad_fin_martes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "comida_inicio_miercoles", nullable = false)
    private LocalTime comida_inicio_miercoles;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_miercoles", nullable = false)
    private LocalTime disponibilidad_inicio_miercoles;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_miercoles", nullable = false)
    private LocalTime disponibilidad_fin_miercoles;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "comida_inicio_jueves", nullable = false)
    private LocalTime comida_inicio_jueves;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_jueves", nullable = false)
    private LocalTime disponibilidad_inicio_jueves;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_jueves", nullable = false)
    private LocalTime disponibilidad_fin_jueves;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "comida_inicio_viernes", nullable = false)
    private LocalTime comida_inicio_viernes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_viernes", nullable = false)
    private LocalTime disponibilidad_inicio_viernes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_viernes", nullable = false)
    private LocalTime disponibilidad_fin_viernes;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "comida_inicio_sabado", nullable = false)
    private LocalTime comida_inicio_sabado;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_inicio_sabado", nullable = false)
    private LocalTime disponibilidad_inicio_sabado;

    @NotNull(message = "El campo hora es obligatorio")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "disponibilidad_fin_sabado", nullable = false)
    private LocalTime disponibilidad_fin_sabado;


}
