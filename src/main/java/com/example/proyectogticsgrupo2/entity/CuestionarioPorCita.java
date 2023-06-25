package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private Integer estado; //1---> cuestionario completado; 0 ------> cuestionario pendiente

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r1;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r2;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r3;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r4;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r5;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r6;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r7;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r8;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r9;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r10;

    @NotBlank(message = "Ingrese una respuesta válida")
    private String r11;

    private LocalDateTime fecha_enviado;
    private LocalDateTime fecha_completado;
}
