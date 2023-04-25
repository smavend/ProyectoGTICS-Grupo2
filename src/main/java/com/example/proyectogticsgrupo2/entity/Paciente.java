package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(name="id_paciente")
    private String idPaciente;
    private String nombre;
    private String apellidos;
    @Column (nullable = false)
    private int estado; //no activo: 0, activo: 1,invitado: 2, registrado:3, agendado: 4, en consulta: 5
    @ManyToOne
    @JoinColumn(name="seguro_id_seguro")
    private Seguro seguro;
    private String telefono;
    @ManyToOne
    @JoinColumn(name="administrativo_id_administrativo")
    private Administrativo administrativo;
    private String correo;
    private String fotoname;
    private String fotocontenttype;
    private byte[] foto;

    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito", nullable = false)
    private Distrito distrito;
    @Column (nullable = false)
    private String direccion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechanacimiento;
    private String genero;
    private LocalDateTime fecharegistro;

    public String getNombreYApellido(){
        String[] nombres = this.getNombre().split(" ");
        String[] apellidos = this.getApellidos().split(" ");
        return nombres[0] + " " + apellidos[0];
    }

}
