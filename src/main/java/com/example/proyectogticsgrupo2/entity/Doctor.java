package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @Column(name = "id_doctor",nullable = false)
    private String idDoctor;

    @Lob
    @Column(nullable = false)
    private byte[] foto;
}
