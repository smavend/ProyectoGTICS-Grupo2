package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="horario")
public class Horario {

    @Id
    private String id_horario;
    private String comida_inicio;
    private String comida_fin;
    private String disponibilidad_inicio;
    private String disponibilidad_fin;

}
