package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "superadmin")
@Setter
@Getter
public class SuperAdmin implements Serializable {
    @Id
    @Column(name = "id_superadmin")
    private String idSuperadmin;

    private String nombre;
    private String apellidos;
    private String correo;
}
