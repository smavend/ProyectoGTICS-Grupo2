package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "superadmin")
@Setter
@Getter
public class SuperAdmin {
    @Id
    @Column(name = "id_superadmin")
    private String idSuperadmin;

    private String nombre;
    private String apellido;
    private String correo;
}
