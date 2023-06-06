package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cuestionario_x_cita")
public class CuestionarioPorCita {
    @EmbeddedId
    private CuestionarioPorCitaID id;

    @MapsId(value = "idCuestionario")
    @ManyToOne
    @JoinColumn(name = "cuestionario_id_cuestionario")
    private Cuestionario cuestionario;

    @MapsId(value = "idCita")
    @ManyToOne
    @JoinColumn(name = "cita_id_cita")
    private Cita cita;

    private Integer opcion_inicio_sesion;
    private Integer estado;
    private String r1;
    private String r2;
    private String r3;
    private String r4;
    private String r5;
    private String r6;
    private String r7;
    private String r8;
    private String r9;
    private String r10;
    private String r11;
    private LocalDateTime fecha_enviado;
    private LocalDateTime fecha_completado;
}
