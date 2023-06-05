package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CuestionarioPorCita {
    @EmbeddedId
    private PacientePorConsentimientoID id;
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
}
