package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "alergias")
public class Alergia {
    @Id
    @Column(name = "id_alergia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlergia;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "paciente_id_paciente")
    private Paciente paciente;
}
